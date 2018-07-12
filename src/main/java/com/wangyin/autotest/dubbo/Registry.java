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
