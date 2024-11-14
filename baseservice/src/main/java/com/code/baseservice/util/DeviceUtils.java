package com.code.baseservice.util;

import javax.servlet.http.HttpServletRequest;

public class DeviceUtils {

    // 定义常见的移动设备标识符
    private static final String[] MOBILE_DEVICE_IDENTIFIERS = {
            "android", "iphone", "ipad", "ipod", "blackberry", "windows phone", "opera mini", "opera mobi", "iemobile", "mobile", "kindle", "silk", "playbook"
    };

    /**
     * 判断请求是否来自移动设备
     * @param request HttpServletRequest 请求对象
     * @return true 表示是移动设备，false 表示不是
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) {
            return false;  // 无法获取 User-Agent 时返回 false
        }

        userAgent = userAgent.toLowerCase();  // 转换为小写，方便匹配

        // 遍历常见的移动设备标识符，如果匹配到，则认为是移动设备
        for (String mobileIdentifier : MOBILE_DEVICE_IDENTIFIERS) {
            if (userAgent.contains(mobileIdentifier)) {
                return true;
            }
        }

        return false;  // 如果没有匹配到任何移动设备标识符，返回 false
    }
}
