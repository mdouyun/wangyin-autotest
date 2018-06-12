/*
 * @(#)ZKClient  1.0 2015-03-19
 *
 * Copyright 2009 chinabank payment All Rights Reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Author Email: yfchenyun@jd.com
 */
package com.wangyin.autotest.zk;

import com.wangyin.autotest.dto.DubboProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO.
 *
 * @author chenyun313@gmail.com, 2015-03-19.
 * @version 1.0
 * @since 1.0
 */
public class ZookeeperClient {

    private CuratorFramework zkClient;

    private String address;

    public ZookeeperClient(String zkServers, String namespace,
                           int sessionTimeout, int connectionTimeout) throws Exception {
        this.address = zkServers;
        zkClient = CuratorFrameworkFactory.builder().connectString(zkServers).namespace(namespace)
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 2000))
                .sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout).build();
        zkClient.start();
    }

    public List<DubboProvider> listInterfaceProviders(String interfaceName) throws Exception {
        String path = interfaceName + "/providers";
        List<String> list = zkClient.getChildren().forPath(path);

        List<DubboProvider> providers = new ArrayList<DubboProvider>();

        for(String url : list) {

            DubboProvider provider = new DubboProvider(URLDecoder.decode(url, "utf-8"));
            providers.add(provider);
        }

        return providers;
    }

    public void close() {
        if(this.zkClient != null) {
            this.zkClient.close();
        }
    }

    @Override
    public int hashCode() {
        return this.address.hashCode();
    }
}
