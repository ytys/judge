package com.github.zhanhb.judge.util;

import org.springframework.web.context.request.WebRequest;

public class AjaxUtils {

    public static boolean isAjaxRequest(WebRequest webRequest) {
        return "XMLHttpRequest".equals(webRequest.getHeader("X-Requested-With"));
    }

    public static boolean isAjaxUploadRequest(WebRequest webRequest) {
        return webRequest.getParameter("ajaxUpload") != null;
    }

    private AjaxUtils() {
        throw new AssertionError();
    }

}
