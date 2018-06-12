package com.wangyin.mpp.knife;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wangyin.npp.payment.dto.PaymentDepositRequest;
import com.wangyin.npp.payment.facade.PaymentService;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class MethodClassTest extends TestCase {

    public void testJson() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        PaymentDepositRequest request = new PaymentDepositRequest();

        Class cls = Class.forName(PaymentService.class.getName());

        Method[] methods = cls.getMethods();
        Method findMethod = null;
        for(Method method : methods) {
            if(StringUtils.equals(method.getName(), "deposit")) {
                findMethod = method;
                break;
            }
        }

        if(findMethod == null) {
            return ;
        }

        Class[] argTypes = findMethod.getParameterTypes();

        List<Object> parameters = new ArrayList<Object>();
        for(Class argCls : argTypes) {
            Object o = newInstance(argCls);
            parameters.add(o);
        }

        System.out.println((JSON.toJSONString(parameters, SerializerFeature.WriteClassName)));
    }

    private Object newInstance(Class cls) {

        String packageName = cls.getName();

        if(cls.isInterface()) {
            return null;
        } else if(cls.isArray()) {
            try {
                Object fieldValue = cls.newInstance();
                return fieldValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(cls.isEnum()) {
            return null;
        } else if(cls.isPrimitive()) {

            if(StringUtils.equals(packageName, "boolean")) {
                return true;
            } else if(StringUtils.equals(packageName, "byte")) {
                return Byte.MIN_VALUE;
            } else if(StringUtils.equals(packageName, "char")) {
                return 'c';
            } else if(StringUtils.equals(packageName, "double")) {
                return Double.MIN_VALUE;
            } else if(StringUtils.equals(packageName, "float")) {
                return Float.MIN_VALUE;
            } else if(StringUtils.equals(packageName, "int")) {
                return Integer.MIN_VALUE;
            } else if(StringUtils.equals(packageName, "long")) {
                return Long.MIN_VALUE;
            } else if(StringUtils.equals(packageName, "short")) {
                return Short.MIN_VALUE;
            }

        } else if(StringUtils.equals(packageName, "java.lang.String")) {
            return "string";
        } else if(StringUtils.startsWith(packageName, "java")) {
            try {
                Object fieldValue = cls.newInstance();
                return fieldValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                Object fieldValue = cls.newInstance();
                Field[] fields = cls.getDeclaredFields();
                for(Field f : fields) {

                    if(!Modifier.isStatic(f.getModifiers())) {
                        f.setAccessible(true);
                        f.set(fieldValue, newInstance(f.getType()));
                    }
                }

                return fieldValue;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }

}
