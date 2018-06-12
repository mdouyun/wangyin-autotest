package com.wangyin.autotest.task;

import com.wangyin.commons.util.Logger;
import com.wangyin.autotest.dto.ZookeeperAddress;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.autotest.manager.FileManager;
import com.wangyin.autotest.manager.ZookeeperManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将内存中的配置数据同步到文件中的定时任务，一分钟执行一次.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class FileSynchronizeTask {

    @Autowired
    private DubboManager dubboManager;

    @Autowired
    private ZookeeperManager zookeeperManager;

    @Autowired
    private FileManager fileManager;

    private static final Logger LOGGER = new Logger();

    public void execute() {

        LOGGER.info("***************File synchronize task start***************");

        try {

            int zkFlag = zookeeperManager.getModifyFlag().get();
            if(zkFlag > 0) {
                // 保存zookeeper信息
                List<ZookeeperAddress> list = zookeeperManager.getAll();
                Map<String, String> map = new HashMap<String, String>();
                for (ZookeeperAddress address : list) {
                    map.put(address.getId(), address.getAddress());
                }

                fileManager.storeZookeeperConf(map);

                zookeeperManager.getModifyFlag().compareAndSet(zkFlag, 0);
            }
        } catch (IOException e) {
            LOGGER.error("Store zookeeper config IOException", e);
        }

        try {
            int dubboFlag = dubboManager.getModifyFlag().get();
            if(dubboFlag > 0) {
                // 保存dubbo信息
                Map<String, String> map = new HashMap<String, String>();

                Map<String, String> zkMap = dubboManager.getAllZookeeperConfig();
                for (String key : zkMap.keySet()) {
                    map.put(key + "-zk", zkMap.get(key));
                }

                Map<String, String> dubboMap = dubboManager.getAllDubboConfig();
                for (String key : dubboMap.keySet()) {
                    map.put(key + "-dubbo", dubboMap.get(key));
                }

                fileManager.storeDubboConf(map);

                dubboManager.getModifyFlag().compareAndSet(dubboFlag, 0);
            }
        } catch (IOException e) {
            LOGGER.error("Store dubbo config IOException", e);
        }


        LOGGER.info("***************File synchronize task end***************");
    }

}
