package com.wangyin.autotest.converter;

/**
 * json串转换器.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public interface JsonConverter {

    public Class getConvertType();

    public String convert2Json(Object initValue);

    public String handleJson(String json);

}
