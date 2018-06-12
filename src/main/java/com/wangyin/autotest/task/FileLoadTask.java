package com.wangyin.autotest.task;

import com.wangyin.commons.util.Logger;
import com.wangyin.autotest.dto.ZookeeperAddress;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.autotest.manager.FileManager;
import com.wangyin.autotest.manager.ZookeeperManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * 将配置文件中的数据加载到内存中，系统启动时加载一次.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class FileLoadTask {

    @Autowired
    private DubboManager dubboManager;

    @Autowired
    private ZookeeperManager zookeeperManager;

    @Autowired
    private FileManager fileManager;

    private static final Logger LOGGER = new Logger();

    public void execute() throws IOException {
        LOGGER.info("***************File load task start***************");

        Map<String, String> zookeeperMap = fileManager.getZookeeperConf();
        for(String key : zookeeperMap.keySet()) {
            ZookeeperAddress address = new ZookeeperAddress();
            address.setId(key);
            address.setAddress(zookeeperMap.get(key));
            try {
                zookeeperManager.addOrModifyZookeeper(address);
            } catch (Exception e) {
                LOGGER.error("Add zookeeper conf exception, skip this one", address, e);
            }
        }

        Map<String, String> dubboMap = fileManager.getDubboConf();
        for(String key : dubboMap.keySet()) {

            key = StringUtils.removeEnd(key, "-zk");
            key = StringUtils.removeEnd(key, "-dubbo");

            String zookeeperAddress = dubboMap.get(key + "-zk");
            String dubboUrl = dubboMap.get(key + "-dubbo");

            try {
                dubboManager.addDubboServer(dubboUrl, zookeeperAddress);
            } catch (Exception e) {
                LOGGER.error("Add dubbo conf exception, skip this one", key, e);
            }
        }

        LOGGER.info("***************File load task end***************");
    }


}
