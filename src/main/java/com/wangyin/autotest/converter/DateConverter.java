package com.wangyin.autotest.converter;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class DateConverter implements JsonConverter {

    @Override
    public Class getConvertType() {
        return Date.class;
    }

    @Override
    public String convert2Json(Object initValue) {

        if(initValue != null && initValue instanceof Date) {
            return String.valueOf(((Date) initValue).getTime());
        }

        return "new Date()";
    }

    @Override
    public String handleJson(String json) {
        Date date = new Date();

        return StringUtils.replace(json, "new Date()", String.valueOf(date.getTime()));
    }
}
