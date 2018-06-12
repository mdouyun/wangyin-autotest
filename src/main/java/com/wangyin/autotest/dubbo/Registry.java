/*
 * @(#)Registry  1.0 2015-03-19
 *
 * Copyright 2009 chinabank payment All Rights Reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Author Email: yfchenyun@jd.com
 */
package com.wangyin.autotest.dubbo;

import com.alibaba.dubbo.config.RegistryConfig;

/**
 * Dubbo注册器.
 *
 * @author chenyun313@gmail.com, 2015-03-19.
 * @version 1.0
 * @since 1.0
 */
public class Registry {

    public RegistryConfig init(String id, String zkAddress) {

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zkAddress);
        registryConfig.setId(id);

        return registryConfig;
    }

}
