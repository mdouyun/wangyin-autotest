package com.wangyin.autotest.http;

import com.alibaba.dubbo.common.json.JSON;
import com.wangyin.autotest.dto.JsonData;
import com.wangyin.autotest.utils.HttpClientUtils;
import com.wangyin.commons.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Http case相关的Controller，包含case的生成、测试及保存.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
@RequestMapping("/http")
public class HttpCaseController {

    private static final Logger LOGGER = new Logger();

    @RequestMapping("/sendRequest")
    @ResponseBody
    public JsonData<String> sendRequest(String url, String method, String request, String mimeType, String charset, String header, String form) {
        JsonData<String> data = new JsonData<String>();

        try {
            String result = null;

            Map<String, String> headerMap = JSON.parse(header, HashMap.class);

            data.setSuccess(true);

            if(StringUtils.equalsIgnoreCase(method, "get")) {
                result = HttpClientUtils.get(url, headerMap);
            } else if(StringUtils.equalsIgnoreCase(method, "post")
                    && StringUtils.equalsIgnoreCase(mimeType, "multipart/form-data")) {

                Map<String, String> formMap = JSON.parse(form, HashMap.class);
                result = HttpClientUtils.formPost(url, headerMap, charset, formMap);
            } else if(StringUtils.equalsIgnoreCase(method, "post")) {
                result = HttpClientUtils.textPost(url, headerMap, charset, request, mimeType);
            } else {
                data.setError("参数传入有误");
            }

            data.setData(result);
            return data;

        } catch (Exception e) {
            data.setSuccess(false);
            data.setError(e.getMessage());

            LOGGER.error("发送Http请求异常！", e);
        }

        return data;
    }

}
