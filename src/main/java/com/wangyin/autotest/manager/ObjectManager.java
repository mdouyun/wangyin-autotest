package com.wangyin.autotest.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangyin.autotest.converter.DateConverter;
import com.wangyin.autotest.converter.JsonConverter;
import com.wangyin.autotest.converter.ListConverter;
import com.wangyin.autotest.converter.MapConverter;
import com.wangyin.commons.util.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象与json的转换.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ObjectManager {

    private static final Logger LOGGER = new Logger();

    private static final Map<Class, JsonConverter> CONVERTER_MAP = new HashMap<Class, JsonConverter>();

    static {
        JsonConverter dateConverter = new DateConverter();
        CONVERTER_MAP.put(dateConverter.getConvertType(), dateConverter);

        JsonConverter mapConverter = new MapConverter();
        CONVERTER_MAP.put(mapConverter.getConvertType(), mapConverter);

        JsonConverter listConverter = new ListConverter();
        CONVERTER_MAP.put(listConverter.getConvertType(), listConverter);
    }

    public static String toJSON(Class cls) {

        String className = cls.getName();

        if(cls.isEnum() || cls.isInterface()
                || cls.isArray() || cls.isPrimitive()
//                || StringUtils.startsWith(className, "java")
                || Modifier.isAbstract(cls.getModifiers())) {
            return null;
        }

        try {
            Object o = cls.newInstance();
            Map<String, String> property = BeanUtils.describe(o);

            property.remove("class");

            StringBuilder json = new StringBuilder("{");
            json.append("\"@type\":\"").append(className).append("\",");

            for(String key : property.keySet()) {
                json.append("\"").append(key).append("\":");
                String value = null;
                try {
                    Field field = cls.getDeclaredField(key);

                    Object beanValue = PropertyUtils.getProperty(o, key);

                    if(field.getType().isPrimitive() || field.getType() == String.class) {
                        value = property.get(key);
                    } else {

                        JsonConverter converter = CONVERTER_MAP.get(field.getType());

                        if(converter != null) {
                            value = converter.convert2Json(beanValue);
                        } else {
                            value = toJSON(field.getType());
                        }

                    }

                } catch (Exception e) {
                    LOGGER.warn("Field [", key, "] convert exception:", e.getMessage());
                }

                json.append(value).append(",");
            }

            int i = json.lastIndexOf(",");
            if(i > 0) {
                json.deleteCharAt(i);
            }
            json.append("}");

            return json.toString();
        } catch (Exception e) {
            LOGGER.warn("Class [", className, "] convert exception:", e.getMessage());
        }

        return null;
    }

    public static Object[] toObject(String json) {

        for(Class cls : CONVERTER_MAP.keySet()) {
            json = CONVERTER_MAP.get(cls).handleJson(json);
        }

        JSONArray array = JSON.parseArray(json);
        Object[] args = new Object[array.size()];

        for(int i = 0; i < array.size(); i++) {
            JSONObject node = (JSONObject) array.get(i);
            args[i] = node.get("参数-" + (i+1));
        }

        return args;
    }


}
