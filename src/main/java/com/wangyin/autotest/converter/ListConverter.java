package com.wangyin.autotest.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * List的转换.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ListConverter implements JsonConverter {

    @Override
    public Class getConvertType() {
        return List.class;
    }

    @Override
    public String convert2Json(Object initValue) {
        if(initValue != null && initValue instanceof List) {
            return JSON.toJSONString(initValue, SerializerFeature.WriteClassName);
        }

        return "{\"@type\":\"java.util.ArrayList\"}";
    }

    @Override
    public String handleJson(String json) {
        return json;
    }
}
