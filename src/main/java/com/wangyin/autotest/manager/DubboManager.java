package com.wangyin.autotest.manager;

import com.wangyin.autotest.dto.DubboProvider;
import com.wangyin.autotest.dubbo.DubboBeanFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Service
public class DubboManager {

    @Autowired
    private DubboBeanFactory dubboBeanFactory;

    private static final ConcurrentHashMap<String, String> DUBBO_ZK_CONFIG = new ConcurrentHashMap<String, String>(500);

    private static final ConcurrentHashMap<String, String> DUBBO_URL_CONFIG = new ConcurrentHashMap<String, String>(500);

    private static final AtomicInteger FLAG = new AtomicInteger(0);

    public void addDubboServer(String dubboUrl, String zookeeperAddress)
            throws ClassNotFoundException {

        DubboProvider dubboProvider = new DubboProvider(dubboUrl);

        Class interfaceCls = Class.forName(dubboProvider.getInterfaceName());

        if(!StringUtils.startsWithIgnoreCase(zookeeperAddress, "zookeeper://")) {
            zookeeperAddress = StringUtils.replaceOnce(zookeeperAddress, ",", "?backup=");
            zookeeperAddress = "zookeeper://" + zookeeperAddress;
        }

        dubboBeanFactory.registry(interfaceCls, dubboProvider.getVersion(),
                dubboProvider.getGroup(), zookeeperAddress);

        String key = this.getDubboUrlKey(dubboProvider);

        DUBBO_ZK_CONFIG.put(key, zookeeperAddress);
        DUBBO_URL_CONFIG.put(key, dubboUrl);
        FLAG.getAndIncrement();
    }

    public Object getDubboProvider(DubboProvider dubboProvider) throws ClassNotFoundException {

        String key = this.getDubboUrlKey(dubboProvider);

        String zkAddress = DUBBO_ZK_CONFIG.get(key);

        if(StringUtils.isEmpty(zkAddress)) {
            return null;
        }

        Class interfaceCls = Class.forName(dubboProvider.getInterfaceName());

        return dubboBeanFactory.registry(interfaceCls, dubboProvider.getVersion(),
                dubboProvider.getGroup(), zkAddress);
    }

    public String getZookeeperAddress(String dubboUrl) {
        DubboProvider dubboProvider = new DubboProvider(dubboUrl);
        String key = this.getDubboUrlKey(dubboProvider);

        return DUBBO_ZK_CONFIG.get(key);
    }

    private String getDubboUrlKey(DubboProvider dubboProvider) {
        StringBuilder key = new StringBuilder(dubboProvider.getServerUrl());
        key.append("-").append(dubboProvider.getGroup());
        key.append("-").append(dubboProvider.getVersion());

        return key.toString();
    }

    public void delete(String dubboUrl) {
        DubboProvider dubboProvider = new DubboProvider(dubboUrl);

        String key = this.getDubboUrlKey(dubboProvider);

        DUBBO_ZK_CONFIG.remove(key);
        DUBBO_URL_CONFIG.remove(key);
        FLAG.getAndIncrement();
    }

    public AtomicInteger getModifyFlag() {
        return FLAG;
    }

    public Map<String, String> getAllZookeeperConfig() {
        return DUBBO_ZK_CONFIG;
    }

    public Map<String, String> getAllDubboConfig() {
        return DUBBO_URL_CONFIG;
    }

}
