package com.wangyin.autotest.manager;

import com.wangyin.autotest.dto.ZookeeperAddress;
import com.wangyin.autotest.dto.DubboProvider;
import com.wangyin.autotest.zk.ZookeeperClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Zookeeper管理服务.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ZookeeperManager {

    private String namespace;

    private int sessionTimeout;

    private int connectionTimeout;

    private static final ConcurrentHashMap<String, String> ZOOKEEPER_CONFIG = new ConcurrentHashMap<String, String>(500);

    private static final ConcurrentHashMap<String, ZookeeperClient> ZOOKEEPER_CLIENT = new ConcurrentHashMap<String, ZookeeperClient>(500);

    private static final AtomicInteger FLAG = new AtomicInteger(0);

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public List<ZookeeperAddress> getAll() {
        List<ZookeeperAddress> list = new ArrayList<ZookeeperAddress>();

        for(String key : ZOOKEEPER_CONFIG.keySet()) {
            ZookeeperAddress address = new ZookeeperAddress();
            address.setId(key);
            address.setAddress(ZOOKEEPER_CONFIG.get(key));

            list.add(address);
        }

        return list;
    }

    public ZookeeperClient getById(String id) {
        return ZOOKEEPER_CLIENT.get(id);
    }

    public String getAddressById(String id) {
        return ZOOKEEPER_CONFIG.get(id);
    }

    public void addOrModifyZookeeper(ZookeeperAddress address) throws Exception {

        if(ZOOKEEPER_CLIENT.containsKey(address.getId())) {
            ZookeeperClient client = ZOOKEEPER_CLIENT.get(address.getId());
            client.close();
        }

        ZookeeperClient client = new ZookeeperClient(address.getAddress(),
                this.namespace, this.sessionTimeout, this.connectionTimeout);

        ZOOKEEPER_CONFIG.put(address.getId(), address.getAddress());
        ZOOKEEPER_CLIENT.put(address.getId(), client);
        FLAG.getAndIncrement();
    }

    public void deleteZookeeper(String id) {

        if(ZOOKEEPER_CLIENT.containsKey(id)) {
            ZookeeperClient client = ZOOKEEPER_CLIENT.get(id);
            client.close();
        }

        ZOOKEEPER_CONFIG.remove(id);
        ZOOKEEPER_CLIENT.remove(id);
        FLAG.getAndIncrement();
    }

    public AtomicInteger getModifyFlag() {
        return FLAG;
    }

    public List<DubboProvider> getDubboProvider(String interfaceClass, String zkId) throws Exception {

        if(ZOOKEEPER_CLIENT.containsKey(zkId)) {
            ZookeeperClient client = ZOOKEEPER_CLIENT.get(zkId);
            List<DubboProvider> list = client.listInterfaceProviders(interfaceClass);
            return list;
        } else {
            return null;
        }
    }
}
