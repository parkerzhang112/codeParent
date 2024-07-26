package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.PayTypeRateConstans;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfAgentDao;
import com.code.baseservice.dto.backapi.OperaAgentParams;
import com.code.baseservice.dto.frontapi.LoginDto;
import com.code.baseservice.dto.frontapi.RegisterDto;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.ZfAgentRechargeOrderService;
import com.code.baseservice.service.ZfAgentRecordService;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfAgentTransService;
import com.code.baseservice.util.MD5Util;
import com.code.baseservice.util.StringUtils;
import com.code.baseservice.util.Telegram;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * (ZfAgent)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 22:49:50
 */
@Service("zfAgentService")
@Slf4j
public class ZfAgentServiceImpl implements ZfAgentService {
    @Resource
    private ZfAgentDao zfAgentDao;

    @Autowired
    private ZfAgentTransService zfAgentTransService;

    @Autowired
    private ZfAgentRecordService zfAgentRecordService;

    @Value("${app.between_amount:800}")
    private BigDecimal betweenAmount;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ZfAgentRechargeOrderService zfAgentRechargeOrderService;

    @Autowired
    private ZfAgentService zfAgentService;

    private static final ConcurrentHashMap<String, ReentrantLock> lockPointMap = new ConcurrentHashMap<>();


    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    @Override
    public ZfAgent queryById(Integer agentId) {
        return this.zfAgentDao.queryById(agentId);
    }


    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public int update(ZfAgent zfAgent) {
       return this.zfAgentDao.update(zfAgent);

    }

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentCreditAmount(ZfRecharge zfRecharge, Integer agentId) {
        RLock rLock = redissonClient.getLock("agentid"+agentId);
        try{
            log.info("开始更新代理额度  {} 时间 {}", zfRecharge.getMerchantOrderNo(), System.currentTimeMillis());
            if(rLock.tryLock(5,10,TimeUnit.SECONDS)){
                long start  = System.currentTimeMillis();
                log.info("计算代理可收 订单号 {}", zfRecharge.getMerchantOrderNo());
                ZfAgent zfAgent = zfAgentDao.queryById(agentId);
                ZfAgent zfAgent1 = new ZfAgent();
                zfAgent1.setAgentId(zfAgent.getAgentId());
                zfAgent.setAgentId(agentId);
                if(zfRecharge.getOrderStatus() == 1){
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().subtract(zfRecharge.getPayAmount()));
                    zfAgent1.setAcceptAmount(BigDecimal.ZERO.subtract(zfRecharge.getPayAmount()));
                }else if(zfRecharge.getOrderStatus() == 3 ){
                    zfAgent1.setAcceptAmount(BigDecimal.ZERO.add(zfRecharge.getPayAmount()));
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfRecharge.getPayAmount()));
                }else if(zfRecharge.getOrderStatus() == 5){
                    ZfAgentTrans zfAgentTrans = zfAgentTransService.queryAddTransBySub(zfRecharge.getMerchantOrderNo());
                    if(zfAgentTrans != null){
                        zfAgent1.setAcceptAmount(BigDecimal.ZERO.add(zfRecharge.getPayAmount()));
                        zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfRecharge.getPayAmount()));
                    }else {
                        log.info("订单无入款减分流水，不给加分 订单号 {}", zfRecharge.getMerchantOrderNo());
                        return;
                    }
                }
                zfAgentTransService.insert(new ZfAgentTrans(zfRecharge, zfAgent,  BigDecimal.ZERO));
                int r =   zfAgentDao.updateAgentFee(zfAgent1);
                long end  = System.currentTimeMillis();
                if(start -end > 10000){
                    log.info("超时订单 {} 超时时间 {}", zfRecharge.getMerchantOrderNo(), start-end);
                }
                if(r == 0){
                    Telegram telegram = new Telegram();
                    telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                    log.error("更新代理费用失败");
                }
            }else {
                Telegram telegram = new Telegram();
                telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                log.error("更新代理余额异常 {} ",zfRecharge.getMerchantOrderNo());
                throw  new RuntimeException();
            }


        }catch (Exception e){
            Telegram telegram = new Telegram();
            telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
            log.error("更新代理余额异常 {}", e);
            throw  new RuntimeException(e);
        }finally {
            log.info("释放锁时间  {} 时间 {}", zfRecharge.getMerchantOrderNo(), System.currentTimeMillis());
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }

    /**
     * 更新代理可收额度
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentFee(ZfWithdraw zfWithdraw, Integer agentId, BigDecimal fee) {
        RLock rLock = redissonClient.getLock("agentid"+agentId);
        try{
            log.info("开始更新代理额度  {} 时间 {}", zfWithdraw.getMerchantOrderNo(), System.currentTimeMillis());
            if(rLock.tryLock(5,10,TimeUnit.SECONDS)){
                ZfAgent zfAgent = queryById(agentId);
                BigDecimal agentFee =  sumAgentFeeByWithdraw(zfWithdraw.getPayAmount(), zfAgent.getRate());
                long start  = System.currentTimeMillis();
                log.info("计算代理可收 订单号 {}", zfWithdraw.getMerchantOrderNo());
                ZfAgent zfAgent1 = new ZfAgent();
                zfAgent1.setAgentId(zfAgent.getAgentId());
                zfAgent.setAgentId(agentId);
                if(zfWithdraw.getOrderStatus() != 1) {
                    throw  new BaseException(ResultEnum.ORDER_STATUS_ERROR);
                }
                if(zfWithdraw.getAgentId().equals(agentId)){
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfWithdraw.getPaidAmount()).add(agentFee.subtract(fee)));
                    zfAgent1.setAcceptAmount(zfWithdraw.getPaidAmount().add(agentFee.subtract(fee)));
                }else {
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(agentFee.subtract(fee)));
                    zfAgent1.setAcceptAmount((agentFee.subtract(fee)));
                }
                zfAgentRecordService.updateRecord(new ZfAgentRecord(zfWithdraw));
                zfAgentTransService.insert(new ZfAgentTrans(zfWithdraw, zfAgent, agentFee.subtract(fee)));
                long end  = System.currentTimeMillis();
                if(start -end > 10000){
                    log.info("超时订单 {} 超时时间 {}", zfWithdraw.getMerchantOrderNo(), start-end);
                }
                zfAgentDao.updateAgentFee(zfAgent1);
                if(zfAgent.getParentId() != null && zfAgent.getParentId() != 0) {
                    ZfAgent parentAgent = zfAgentDao.queryById(zfAgent.getParentId());
                    log.info("父级代理 {}", zfAgent.getParentId());
                    if (parentAgent == null) {
                        return;
                    }
                    updateAgentFee(zfWithdraw, parentAgent.getAgentId(), agentFee);
                }
            }else {
                log.error("更新代理余额异常 {} ",zfWithdraw.getMerchantOrderNo());
                throw  new RuntimeException();
            }
        }catch (Exception e){
            log.error("更新代理余额异常 {}", e);
            throw  new RuntimeException(e);
        }finally {
            log.info("释放锁时间  {} 时间 {}", zfWithdraw.getMerchantOrderNo(), System.currentTimeMillis());
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }



    /**
     * 计算代理费用
     * @param paidAmount
     * @param zfAgent
     */
    @Override
    public void updateAgentFee(ZfRecharge zfRecharge, Integer agentId,BigDecimal fee) {
        log.info("开始更新代理额度  {} 时间 {}", zfRecharge.getMerchantOrderNo(), System.currentTimeMillis());
        RLock rLock = redissonClient.getLock("agentid"+agentId.toString());
        try {
            if (rLock.tryLock(5,10,TimeUnit.SECONDS)){
                ZfAgent zfAgent = zfAgentService.queryById(agentId);
                log.info("更新代理手续费 {}", zfAgent);

                if (zfAgent == null){
                    log.info("订单完成时 查询代理不存在 {}", zfRecharge.getMerchantId());
                    return;
                }
                ZfAgent updateAgent  = new ZfAgent();
                updateAgent.setAgentId(zfAgent.getAgentId());
                BigDecimal agentFee =  sumAgentFee(zfRecharge, zfAgent.getRate());
                if(agentFee.compareTo(zfRecharge.getMerchantFee())> 0){
                    log.info("代理费用 高于商户费用。不给上分  {}", zfRecharge.getMerchantOrderNo());
                    Telegram telegramUtil = new Telegram();
                    telegramUtil.sendWarrmFeeMessage(zfRecharge, "代理费用超过商户");
                    return;
                }
                if(agentFee.compareTo(BigDecimal.ZERO) < 0 ){
                    log.info("代理费率设置错误 {}", zfAgent);
                    return;
                }
                if(fee.compareTo(BigDecimal.ZERO)>0 && agentFee.compareTo(fee) < 0){
                    log.info("代理费率设置错误 {}", zfAgent);
                    return;
                }
                updateAgent.setBalance(agentFee.subtract(fee));
                zfAgent.setBalance(zfAgent.getBalance().add(agentFee.subtract(fee)));
                //新增代理流水
                zfAgentTransService.insert(new ZfAgentTrans(zfRecharge, zfAgent,  agentFee.subtract(fee)));
                //查询订单是否有代理流水补分记录，如果有说明已经超时补分过
                ZfAgentTrans zfAgentTrans = zfAgentTransService.queryAddTransByOrderNo(zfRecharge.getMerchantOrderNo());
                log.info("代理超时流水 {}", zfAgentTrans);
                if(null != zfAgentTrans && zfAgentTrans.getAgentId().equals(zfAgent.getAgentId())){
                    //扣除积分
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().subtract(zfRecharge.getPayAmount()));
                    updateAgent.setAcceptAmount(BigDecimal.ZERO.subtract(zfRecharge.getPayAmount()));
                    //说明订单自动取消过，这个时候，要新增吧积分重新扣回
                    ZfAgentTrans zfAgentTrans1 = new ZfAgentTrans();
                    zfAgentTrans1.buildFailOrderRollbackBySuccess(zfRecharge,zfAgent);
                    zfAgentTransService.insert(zfAgentTrans1);
                }
                //更新代理余额
                int r = zfAgentDao.updateAgentFee(updateAgent);
                if(r == 0){
                    Telegram telegram = new Telegram();
                    telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                }
                //如果代理费用为0，假设为一级代理，不然则上下级平点位代理，则不管
                if(fee.compareTo(BigDecimal.ZERO) == 0){
                    //新增代理报表
                    zfAgentRecordService.updateRecord(new ZfAgentRecord(zfAgent,  agentFee.subtract(fee), zfRecharge.getPaidAmount()));
                }else {
                    ZfAgentRecord zfAgentRecord = new ZfAgentRecord();
                    zfAgentRecord.buildAgentRecordBySunAgentSuccessAndParent(zfAgent,agentFee.subtract(fee),zfRecharge);
                    zfAgentRecordService.updateRecord(zfAgentRecord);
                }
                if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                    rLock.unlock();
                }
                if(zfAgent.getParentId() != null && zfAgent.getParentId() != 0) {
                    ZfAgent parentAgent = zfAgentDao.queryById(zfAgent.getParentId());
                    log.info("父级代理 {}", zfAgent.getParentId());
                    if (parentAgent == null) {
                        return;
                    }
                    updateAgentFee(zfRecharge, parentAgent.getAgentId(), agentFee);
                }
            }else {
                Telegram telegram = new Telegram();
                telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                log.error("更新代理余额失败, {}", zfRecharge.getMerchantOrderNo());
                throw new RuntimeException();
            }
        }catch (Exception e){
            Telegram telegram = new Telegram();
            telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
            log.error("更新代理余额失败,{} {}",zfRecharge.getMerchantOrderNo(), e);
            throw new RuntimeException(e);
        }finally {
            log.info("结束更新代理额度  {} 时间 {}", zfRecharge.getMerchantOrderNo(), System.currentTimeMillis());
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }

    @Override
    public void operatBalance(OperaAgentParams operaAgentParams) {
        ZfAgent zfAgent1;
        ZfAgentTrans zfAgentTrans = new ZfAgentTrans();
        if(null != operaAgentParams.getAgentCode() ){
             zfAgent1 = zfAgentDao.queryByCode(operaAgentParams.getAgentCode());
        }else {
             zfAgent1 = zfAgentDao.queryById(operaAgentParams.getAgentId());
        }
        ZfAgent zfAgent = new ZfAgent();
        zfAgent.setAgentId(zfAgent1.getAgentId());
        zfAgentTrans.setAgentId(zfAgent1.getAgentId());
        if(null  != zfAgent1.getParentId() && zfAgent1.getParentId() != 0 && !operaAgentParams.getIsAdmin() && !operaAgentParams.getIsFinsh()){
            //给下级加积分扣上级，计算上级积分
            if(operaAgentParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())) {
                ZfAgent zfAgentParent = zfAgentDao.queryById(zfAgent1.getParentId());
                if(zfAgentParent.getAcceptAmount().compareTo(operaAgentParams.getAcceptAmount()) < 0){
                    log.info("余额不够");
                    throw new BaseException(ResultEnum.NO_ENOUGH_AMOUNT);
                }
            }
        }
        if(zfAgent1.getAcceptAmount().compareTo(operaAgentParams.getAcceptAmount())<0
                && operaAgentParams.getTransType() == TransTypeEnum.TRANSFER.getValue()){
            log.info("分数不能扣成负数");
            throw new BaseException(ResultEnum.NO_ENOUGH_AMOUNT);
        }
        zfAgentTrans.setPreBalance(zfAgent1.getBalance());
        zfAgentTrans.setBalance(zfAgent1.getBalance());
        zfAgentTrans.setTransType(operaAgentParams.getTransType());
        zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount());
        if(operaAgentParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())){
            zfAgentTrans.setAcceptAmount(operaAgentParams.getAcceptAmount().add(zfAgent1.getAcceptAmount()));
            zfAgentTrans.setAmount(operaAgentParams.getAcceptAmount());
            zfAgentTrans.setPreAcceptAmount(zfAgent1.getAcceptAmount());
            zfAgent.setAcceptAmount(operaAgentParams.getAcceptAmount());
        }else {
            zfAgent.setAcceptAmount(BigDecimal.ZERO.subtract(operaAgentParams.getAcceptAmount()));
            zfAgentTrans.setAmount(operaAgentParams.getAcceptAmount());
            zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount().subtract(operaAgentParams.getAcceptAmount()));
            zfAgentTrans.setPreAcceptAmount(zfAgent1.getAcceptAmount());

        }
        zfAgentTrans.setTransType(operaAgentParams.getTransType());
        zfAgentTrans.setRemark(operaAgentParams.getRemark() + "操作代码"+ operaAgentParams.getAgentCode());
        if(operaAgentParams.getIsAdmin()){
            zfAgentTrans.setOperType(1);
        }else {
            zfAgentTrans.setOperType(2);
        }
        zfAgentTransService.insert(zfAgentTrans);
        log.info("更新代理信息 {}", zfAgent);
        zfAgentDao.updateAgentFee(zfAgent);
        //如果有上级代理，则返回上级代理增加积分,不能无限制递归增分，只限制一次
        if( !operaAgentParams.getIsFinsh() && (zfAgent1.getParentId() != null && zfAgent1.getParentId() != 0)   && !operaAgentParams.getIsAdmin() ){
            operaAgentParams.setAgentId(zfAgent1.getParentId());
            if(operaAgentParams.getTransType() == TransTypeEnum.TRANSFER.getValue()){
                operaAgentParams.setTransType(TransTypeEnum.RRCHARGE.getValue());
            }else {
                operaAgentParams.setTransType(TransTypeEnum.TRANSFER.getValue());
            }
            operaAgentParams.setIsFinsh(true);
            operaAgentParams.setRemark("给下级下分扣除" + operaAgentParams.getAcceptAmount());
            log.info("开始操作上级代理积分 {}", operaAgentParams);
            operatBalance(operaAgentParams);
        }
    }

    @Override
    public void changeBalance(OperaAgentParams operaAgentParams) {
        ZfAgentTrans zfAgentTrans = new ZfAgentTrans();
        ZfAgent zfAgent = new ZfAgent();
        zfAgent.setAgentId(operaAgentParams.getAgentId());
        zfAgent.setAcceptAmount(operaAgentParams.getAcceptAmount());
        zfAgentTrans.setAgentId(operaAgentParams.getAgentId());
        ZfAgent zfAgent1 = zfAgentDao.queryById(operaAgentParams.getAgentId());
        if(zfAgent1.getBalance().compareTo(operaAgentParams.getAmount()) < 0){
            throw new BaseException(ResultEnum.NO_COMMISSION_AMOUNT);
        }
        zfAgent.setBalance(zfAgent1.getBalance().subtract(operaAgentParams.getAmount()));
        zfAgent.setAcceptAmount(zfAgent1.getAcceptAmount().add(operaAgentParams.getAmount()));
        zfAgentTrans.setPreBalance(zfAgent1.getBalance());
        zfAgentTrans.setBalance(zfAgent1.getBalance().subtract(operaAgentParams.getAmount()));
        zfAgentTrans.setTransType(TransTypeEnum.INTERNAL.getValue());
        zfAgentTrans.setAmount(operaAgentParams.getAmount());
        zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount().add(operaAgentParams.getAmount()));
        zfAgentTrans.setPreAcceptAmount(zfAgent1.getAcceptAmount());

        zfAgentTrans.setRemark("佣金转积分金额");
        zfAgentTransService.insert(zfAgentTrans);
        zfAgentDao.update(zfAgent);
    }

    @Override
    public void recharge(OperaAgentParams operaAgentParams) {
       ZfAgentRechargeOrder zfAgentRechargeOrder =  zfAgentRechargeOrderService.queryById(operaAgentParams.getOrderNo());
        if(zfAgentRechargeOrder.getOrderStatus() != 2){
            throw new BaseException(ResultEnum.ORDER_STATUS_ERROR);
        }
        zfAgentRechargeOrder.setOrderStatus(3);
        zfAgentRechargeOrder.setPaidAmount(operaAgentParams.getAmount());
        zfAgentRechargeOrder.setRemark(operaAgentParams.getRemark());
        zfAgentRechargeOrder.setUpdateby(operaAgentParams.getUpdateBy());
        updateAgentCreditAmount(zfAgentRechargeOrder);
        zfAgentRechargeOrderService.update(zfAgentRechargeOrder);
    }

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentCreditAmount(ZfAgentRechargeOrder zfAgentRechargeOrder) {
        Integer agentId = zfAgentRechargeOrder.getAgentId();
        RLock rLock  = redissonClient.getLock("agent_id"+zfAgentRechargeOrder.getAgentId().toString());
        try{
            log.info("开始更新代理额度  {} 时间 {}", zfAgentRechargeOrder.getOrderNo(), System.currentTimeMillis());
            if(rLock.tryLock(5,10,TimeUnit.SECONDS)){

                log.info("计算代理可收 订单号 {}", zfAgentRechargeOrder.getOrderNo());
                ZfAgent zfAgent = zfAgentDao.queryById(agentId);
                ZfAgent zfAgent1 = new ZfAgent();
                zfAgent1.setAgentId(agentId);
                zfAgent.setAgentId(agentId);
                if(zfAgentRechargeOrder.getOrderStatus() == 3){
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfAgentRechargeOrder.getPaidAmount()));
                    zfAgent1.setAcceptAmount(zfAgentRechargeOrder.getPayAmount());
                }
                zfAgentTransService.insert(new ZfAgentTrans(zfAgentRechargeOrder, zfAgent));
                zfAgentDao.updateAgentFee(zfAgent1);
            }else {
                log.error("终端异常 订单号 {}",zfAgentRechargeOrder.getOrderNo());
            }
        }catch (Exception e){
            log.error("更新代理余额异常 {}", e);
        }finally {
            log.info("释放锁时间  {} 时间 {}", zfAgentRechargeOrder.getOrderNo(), System.currentTimeMillis());
            rLock.unlock();
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }

    @Override
    public ZfAgent queryByAcount(String account){
        List<ZfAgent> zfAgents =  zfAgentDao.queryByAccount(account);
        if(zfAgents.size() > 1){
            throw  new BaseException(ResultEnum.LOGIN_ERR);
        }
        return zfAgents.get(0);
    }

    @Override
    public String login(LoginDto loginDto) {
        List<ZfAgent> zfAgents =  zfAgentDao.queryByAccount(loginDto.getUserName());
        if(zfAgents.size() != 1 ){
            throw  new BaseException(ResultEnum.LOGIN_ERR);
        }
        ZfAgent zfAgent = zfAgents.get(0);
        if(!zfAgent.getPwd().equals(encryptPassword(loginDto.getUserName(),loginDto.getPassword(),"11111"))){
            throw  new BaseException(ResultEnum.LOGIN_ERR);
        }
        return zfAgent.getAgentAccount();
    }

    public String encryptPassword(String loginName, String password, String salt)
    {
        return MD5Util.getMD5Str(loginName + password + salt);
    }

    @Override
    public String regsiter(RegisterDto registerDto) {
        List<ZfAgent> zfAgent2 = zfAgentDao.queryByAccount(registerDto.getUserName());
        if(zfAgent2.size() > 0){
            new BaseException(ResultEnum.USER_NAME_IS_EXIST);
        }
        if(!registerDto.getPassword().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$")){
            new BaseException(ResultEnum.PASSWORD_RULE_ERROR);
        }
        //校验密码
        if(!registerDto.getPassword().equals(registerDto.getConfirm_password())){
            new BaseException(ResultEnum.PASSWORD_NO_SAME);
        }
        //校验代理邀请码
        ZfAgent parentAgent = zfAgentDao.queryByCode(registerDto.getInvitationCode());
        if(parentAgent == null){
            new BaseException(ResultEnum.INVITE_CODE_VAILD);
        }
        //获取代理费率
        ZfAgent zfAgent1 = (new ZfAgent()).generateInfoByRegister(registerDto, parentAgent);
        zfAgent1.setAgentId(zfAgentDao.selectMaxId() + 1);
        String random = zfAgent1.getAgentId() +  (StringUtils.createRandomStr1(6- zfAgent1.getAgentId().toString().length())).toUpperCase();

        zfAgent1.setAgentCode(random);
        zfAgent1.setRate(getRate());
        zfAgent1.setGoogleCode( new GoogleAuthenticator().createCredentials().getKey());
        //写入代理
        zfAgentDao.insert(zfAgent1);
        return registerDto.getUserName();
    }

    private String getRate() {
        return "{\"recharge\":{\"rate_type_s_weixin\":\"1\",\"rate_value_d_code\":\"1\",\"rate_value_g_code\":\"2\",\"rate_value_s_code\":\"0\",\"rate_value_s_weixin\":\"0\",\"rate_type\":\"1\",\"rate_type_s_shuzi\":\"1\",\"rate_type_s_code\":\"1\",\"rate_value_weixin\":\"0\",\"rate_type_b_code\":\"1\",\"rate_value_b_code\":\"1\",\"rate_value\":\"0\",\"rate_type_g_code\":\"1\",\"rate_type_d_code\":\"1\",\"rate_type_trans\":\"1\",\"rate_type_weixin\":\"1\",\"rate_value_trans\":\"0\",\"rate_value_s_shuzi\":\"0\"},\"trans\":{\"rate_type\":\"0\",\"rate_value\":\"0\"}}";
    }


    public BigDecimal sumAgentFee(ZfRecharge zfRecharge, String rate) {
        try{
            if(Strings.isEmpty(rate)){
                return  BigDecimal.ZERO;
            }
            JSONObject jsonObject = JSONObject.parseObject(rate);
            if(jsonObject.containsKey("recharge")){
                JSONObject rechargeRate = jsonObject.getJSONObject("recharge");
                String rate_prex = PayTypeRateConstans.getRateString(zfRecharge.getPayType(),zfRecharge.getPaidAmount());
                String rate_type_key = "rate_type" + rate_prex;
                String rate_value_key = "rate_value" + rate_prex;
                if(!Strings.isEmpty(rechargeRate.getString(rate_type_key))&& !Strings.isEmpty(rechargeRate.getString(rate_type_key))){
                    Integer rate_type = rechargeRate.getInteger(rate_type_key);
                    Double rate_value = rechargeRate.getDoubleValue(rate_value_key);
                    if(rate_type == 0){
                        return BigDecimal.valueOf(rate_value);
                    }else{
                        BigDecimal rate1 = BigDecimal.valueOf(rate_value).divide(new BigDecimal("100"));
                        return zfRecharge.getPaidAmount().multiply(rate1);
                    }
                }
            }
            return BigDecimal.ZERO;
        }catch (Exception e){
            log.info("会员手续费异常", e);
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal sumAgentFeeByWithdraw(BigDecimal paidAmount, String rate) {
        try{
            if(Strings.isEmpty(rate)){
                return  BigDecimal.ZERO;
            }
            JSONObject jsonObject = JSONObject.parseObject(rate);
            if(jsonObject.containsKey("trans")){
                JSONObject rechargeRate = jsonObject.getJSONObject("trans");
                if(!Strings.isEmpty(rechargeRate.getString("rate_type"))&& !Strings.isEmpty(rechargeRate.getString("rate_value"))){
                    Integer rate_type = rechargeRate.getInteger("rate_type");
                    Double rate_value = rechargeRate.getDoubleValue("rate_value");
                    if(rate_type == 0){
                        return BigDecimal.valueOf(rate_value);
                    }else{
                        BigDecimal rate1 = BigDecimal.valueOf(rate_value).divide(new BigDecimal("100"));
                        return paidAmount.multiply(rate1);
                    }
                }
            }
            return BigDecimal.ZERO;
        }catch (Exception e){
            log.info("会员手续费异常", e);
            return BigDecimal.ZERO;
        }
    }

}
