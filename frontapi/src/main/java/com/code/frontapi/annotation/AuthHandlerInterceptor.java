package com.code.frontapi.annotation;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.frontapi.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;

    @Value("${token.refreshTime}")
    private Long refreshTime;

    @Value("${token.expiresTime}")
    private Long expiresTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=======进入拦截器========");

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null || token.trim().isEmpty()) {
            log.warn("Token为空或无效");
            throw new BaseException(ResultEnum.USER_NO_LOGIN);
        }

        log.info("Token: {}", token);

        Map<String, String> tokenData;
        try {
            tokenData = tokenUtil.parseToken(token);
        } catch (Exception e) {
            log.error("Token解析失败", e);
            throw new BaseException(ResultEnum.USER_NO_LOGIN);
        }

        String loginName = tokenData.get("loginName");
        long timeOfUse = System.currentTimeMillis() - Long.parseLong(tokenData.get("timeStamp"));

        if (timeOfUse < refreshTime) {
            log.info("Token验证成功");
            return true;
        } else if (timeOfUse >= refreshTime && timeOfUse < expiresTime) {
            String newToken = tokenUtil.getToken(loginName);
            response.setHeader("token", newToken);
            log.info("Token刷新成功: {}", newToken);
            return true;
        } else {
            log.warn("Token已过期");
            throw new BaseException(ResultEnum.USER_NO_LOGIN);
        }
    }
}
