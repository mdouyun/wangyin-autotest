package com.wangyin.mpp.knife;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wangyin.npp.common.part.ThinCustomer;
import com.wangyin.npp.common.trade.SubTradeTypeEnum;
import com.wangyin.npp.payment.dto.PaymentDepositRequest;
import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;
import java.util.HashMap;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class BeanUtilTest extends TestCase {

    public void testBean() throws Exception {

        PaymentDepositRequest request = new PaymentDepositRequest();

        request.setSubTradeType(SubTradeTypeEnum.DEPOSIT);
        request.setCustomer(new ThinCustomer());
        request.setRequestTime(new Date());
        request.setExt(new HashMap<String, Object>());


//        BeanUtils.setProperty(request, "actionType", "SIGN");

        System.out.println(BeanUtils.describe(request));
//
        System.out.println(JSON.toJSONString(request, SerializerFeature.WriteClassName));

        String js = "{\"@type\":\"com.wangyin.npp.payment.dto.PaymentDepositRequest\",\"channelType\" : null ,\"customer\":{},\"expressActiveTag\":false,\"ext\":{\"@type\":\"java.util.HashMap\"},\"subTradeType\":\"DEPOSIT\"}\n";

        Object o = JSON.parse(js);

        System.out.println(o);

//        System.out.println(BeanUtils.describe(request.getPi()));

        BeanUtils.setProperty(request, "tradeNo", "111111");
        System.out.println(request);

        System.out.println(BeanUtils.describe(new HashMap()));

    }

    public void testArray() throws Exception {

        Class a = String[].class;

        Object c = a.newInstance();

        System.out.println(c);
    }


}
