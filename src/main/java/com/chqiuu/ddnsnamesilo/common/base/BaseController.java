package com.chqiuu.ddnsnamesilo.common.base;

import com.chqiuu.ddnsnamesilo.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class BaseController {

    /**
     * 获取当前用户请求对象
     *
     * @return 当前用户请求对象
     */
    public HttpServletRequest getRequest() {
        return SecurityUtils.getHttpServletRequest();
    }

    public String getStringByParameter(String parameter) {
        return SecurityUtils.getHttpServletRequest().getParameter(parameter);
    }

    public Integer getIntByParameter(String parameter) {
        String parameterValue = getStringByParameter(parameter);
        try {
            return Integer.parseInt(parameterValue);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @return 客户端IP地址
     */
    public String getClientIp() {
        return SecurityUtils.getClientIp();
    }

    /**
     * 获取当前用户响应对象
     *
     * @return 当前用户响应对象
     */
    public HttpServletResponse getResponse() {
        return SecurityUtils.getResponse();
    }

    /**
     * 获取当前用户Session
     *
     * @return 当前用户Session
     */
    public HttpSession getSession() {
        return SecurityUtils.getSession();
    }
}
