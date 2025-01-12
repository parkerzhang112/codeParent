package com.code.payapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.PaytypeEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.ZfCodeService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.DeviceUtils;
import com.code.baseservice.util.GeneratorVnQrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/recharge")
public class RechargeController {

    private String prefix = "pay" ;

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfCodeService zfCodeService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            if("test".equals(rechareParams.getName())){
                rechareParams.setName("");
            }
            JSONObject jsonObject = zfRechargeService.create(rechareParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        log.info("订单号  {} 接口返回 {}", rechareParams.getMerchant_order_no(),responseResult.toJsonString());
        return responseResult.toJsonString();
    }

    @ApiOperation("创建订单")
    @PostMapping("/createCard")
    @ResponseBody
    public String createCard(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.createCard(rechareParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("创建订单")
    @PostMapping("/create_a")
    @ResponseBody
    public String createA(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.createA(rechareParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @GetMapping("/order/{orderno}")
    public String detail(@PathVariable("orderno") String orderno, ModelMap modelMap, HttpServletRequest request) {
        ZfRecharge xRecharge = zfRechargeService.queryById(orderno);
        String page = PaytypeEnum.getPayView(xRecharge.getPayType());
        if(xRecharge.getPayType().equals(PaytypeEnum.CARD.getValue())){
            ZfCode zfCode = zfCodeService.queryById(xRecharge.getCodeId());
            List<String> infos = Arrays.asList(zfCode.getAccount().split("\\|"));
            modelMap.put("bank_card_name", zfCode.getName());
            modelMap.put("bank_card_num", infos.get(0));
            modelMap.put("bank_card_type", infos.get(1));
            modelMap.put("bank_address", infos.get(2));
            long now = new Date().getTime();
            long createTime = xRecharge.getCreateTime().getTime();
            long second =  15*60 -(now - createTime)/1000l ;
            modelMap.put("second", second);
            String code = GeneratorVnQrUtil.vietQrGenerate(infos.get(2),infos.get(0),"QRIBFTTA","12", xRecharge.getPayAmount().setScale(0).toString(), xRecharge.getRemark() );
            modelMap.put("code", code);
            if(infos.get(1).toUpperCase().contains("CAKE")){
                if(DeviceUtils.isMobileDevice(request)){
                    page = "vn_cake_card";
                }else {
                    page = "vn_cake_card_pc";
                }
            }
        }else {
            if(xRecharge.getCodeId() != 0){
                ZfCode zfCode = zfCodeService.queryById(xRecharge.getCodeId());
                modelMap.put("code", zfCode);
            }
        }
        modelMap.put("timeout", 10);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(xRecharge.getPayAmount().setScale(0));
        modelMap.put("xrecharge", xRecharge);
        modelMap.put("amount", formattedNumber);
        return  prefix + "/" + page;
    }


    @ApiOperation("查询订单")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/order/getOrder/{orderno}")
    @ResponseBody
    public String getOrderStatus(@PathVariable("orderno") String orderno){
        ResponseResult responseResult = new ResponseResult();
        try {
           JSONObject jsonObject =  zfRechargeService.getOrderStatus(orderno);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("查询订单")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/order/updateName")
    @ResponseBody
    public String updateName(@RequestBody Map<String, Object> map){
        ResponseResult responseResult = new ResponseResult();
        try {
              zfRechargeService.postName(map);
              responseResult.setCode(ResultEnum.SUCCESS.getCode());
        }catch (BaseException e){
            log.error("系统异常 {}", e);
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("查询订单")
    @PostMapping("/view")
    @ResponseBody
    public String view(@RequestBody QueryParams queryParams){
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.query(queryParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }



    // 获取币安现货市场价格数据
    public static JSONArray getBinanceSpotMarketData() {
        String urlString = "https://api.binance.com/api/v1/ticker/24hr";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("HTTP Error Code: " + responseCode);
                return null;
            }

            Scanner sc = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (sc.hasNext()) {
                inline.append(sc.nextLine());
            }
            sc.close();

            // 使用 FastJSON 解析 JSON 数据
            return JSON.parseArray(inline.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 计算涨幅
    public static double calculatePriceChange(double openPrice, double currentPrice) {
        return (currentPrice - openPrice) / openPrice;
    }

    // 获取1小时前的时间戳
    public static long getOneHourAgoTimestamp() {
        long currentTimestamp = System.currentTimeMillis();
        return currentTimestamp - 1 * 60 * 60 * 1000; // 1小时前
    }

    // 找到涨幅超过6%的币种及其涨幅
    public static void findCoinsWithSignificantChange() {
        JSONArray marketData = getBinanceSpotMarketData();
        if (marketData == null) {
            System.out.println("无法获取市场数据。");
            return;
        }

        long oneHourAgoTimestamp = getOneHourAgoTimestamp();
        boolean hasSignificantCoins = false;

        System.out.println("过去1小时内涨幅超过6%的币种及涨幅：");
        for (int i = 0; i < marketData.size(); i++) {
            JSONObject coinData = marketData.getJSONObject(i);
            double openPrice = coinData.getDouble("openPrice");
            double currentPrice = coinData.getDouble("lastPrice");
            long closeTime = coinData.getLong("closeTime");

            // 计算涨幅
            double priceChange = calculatePriceChange(openPrice, currentPrice);

            // 如果是1小时内的数据且涨幅超过6%
            if (closeTime > oneHourAgoTimestamp && priceChange > 0.06) { // 6% = 0.06
                hasSignificantCoins = true;
                System.out.printf("币种: %s, 涨幅: %.2f%%\n", coinData.getString("symbol"), priceChange * 100);
            }
        }

        if (!hasSignificantCoins) {
            System.out.println("过去1小时内没有涨幅超过6%的币种。");
        }
    }

    public static void main(String[] args) {
        findCoinsWithSignificantChange();
    }

}
