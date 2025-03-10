package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.BaseService;
import com.code.baseservice.service.CommonService;
import com.code.baseservice.service.ZfChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {


    @Autowired
    private SanShiServiceImpl sanShiService;

    @Autowired
    private WxYsServiceImpl weixinService;

    @Autowired
    private WxYsXCXServiceImpl wxYsXCXService;

    @Autowired
    private AFeiDfServiceImpl aFeiDfService;
    @Autowired
    private CaiShangServiceImpl caiShangService;

    @Autowired
    private HuiHuangServiceImpl huiHuangService;
    @Autowired
    private ZfChannelService zfChannelService;

    @Autowired
    private ZhaoCaiServiceImpl zhaoCaiService;

    @Autowired
    private PaoPaoServiceImpl paoPaoService;
    @Autowired
    private ShengYaoServiceImpl shengYaoService;

    @Autowired
    private WanHaoServiceImpl wanHaoService;

    @Autowired
    private JuHeYiMaServiceImpl juHeYiMaService;

    @Autowired
    private ZiJiServiceImpl ziJiService;

    public BaseService transToService(String channelCode){
        switch (channelCode){
            case "SANSHI":
                return sanShiService;
            case "WEIXINCODE":
                return weixinService;
            case "AFEIDF":
                return aFeiDfService;
            case "CAISHANG":
                return caiShangService;
            case "HuiHuang":
                return huiHuangService;
            case "ZhaoCai":
                return zhaoCaiService;
            case "PaoPao":
                return paoPaoService;
            case "XiaoChenXu":
                return wxYsXCXService;
            case "ShengYao":
                return shengYaoService;
            case "WanHao":
                return wanHaoService;
            case "JuHeYiMa":
                return juHeYiMaService;
            case "ZiJi":
                return ziJiService;
            default:
                throw new BaseException(ResultEnum.ERROR);
        }
    }

    public BaseService transToServiceByNotify(String channelCode){
        switch (channelCode){
            case "SANSHI":
                return sanShiService;
            case "WEIXINCODE":
                return weixinService;
            case "AFEIDF":
                return aFeiDfService;
            case "CAISHANG":
                return caiShangService;
            case "HuiHuang":
                return huiHuangService;
            case "ZhaoCai":
                return zhaoCaiService;
            case "PaoPao":
                return paoPaoService;
            case "XiaoChenXu":
                return wxYsXCXService;
            case "ShengYao":
                return shengYaoService;
            case "WanHao":
                return wanHaoService;
            case "JuHeYiMa":
                return juHeYiMaService;
            case "ZiJi":
                return ziJiService;
            default:
                throw new BaseException(ResultEnum.ERROR);
        }
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        BaseService baseService = transToService(zfChannel.getChannelCode());
        return  baseService.create(zfChannel, zfRecharge);
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        BaseService baseService = transToService(zfChannel.getChannelCode());
        return  baseService.create(zfChannel, zfWithdraw);
    }

    @Override
    public JSONObject queryByWithdraw(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        BaseService baseService = transToService(zfChannel.getChannelCode());
        return  baseService.queryByWithdraw( zfChannel,  zfWithdraw);
    }


    @Override
    public String notify(ZfRecharge zfRecharge, ZfChannel zfChannel, Map<String, Object> map) {
        BaseService baseService = transToServiceByNotify(zfChannel.getChannelCode());
        return  baseService.notify(zfRecharge, zfChannel, map);
    }


}
