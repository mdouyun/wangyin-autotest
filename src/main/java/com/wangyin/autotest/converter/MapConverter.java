package com.wangyin.autotest.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

/**
 * Map的转换.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class MapConverter implements JsonConverter {

    @Override
    public Class getConvertType() {
        return Map.class;
    }

    @Override
    public String convert2Json(Object initValue) {

        if(initValue != null && initValue instanceof Map) {
            return JSON.toJSONString(initValue, SerializerFeature.WriteClassName);
        }

        return "{\"@type\":\"java.util.HashMap\"}";
    }

    @Override
    public String handleJson(String json) {
        return json;
    }

}
