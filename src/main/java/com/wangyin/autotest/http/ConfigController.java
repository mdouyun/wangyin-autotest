package com.wangyin.autotest.http;

import com.wangyin.autotest.dto.DubboProvider;
import com.wangyin.autotest.dto.JsonData;
import com.wangyin.autotest.dto.JsonPage;
import com.wangyin.autotest.dto.ZookeeperAddress;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.autotest.manager.ZookeeperManager;
import com.wangyin.commons.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 配置管理controller.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    private static final Logger LOGGER = new Logger();

    @Autowired
    private ZookeeperManager zookeeperManager;

    @Autowired
    private DubboManager dubboManager;

    @RequestMapping("zookeeper")
    public String zkIndex(ModelMap modelMap) {

        List<ZookeeperAddress> list = zookeeperManager.getAll();
        modelMap.put("list", list);

        return "config/zookeeper";
    }

    @RequestMapping("/zookeeper/save")
    @ResponseBody
    public JsonData<String> saveZookeeper(ZookeeperAddress address) {
        JsonData<String> data = new JsonData<String>();

        try {
            zookeeperManager.addOrModifyZookeeper(address);
            data.setData("Zookeeper配置添加成功！");
        } catch (Exception e) {
            data.setSuccess(false);
            data.setError(e.getMessage());
            LOGGER.error("Zookeeper配置添加异常!", e);
        }

        return data;
    }

    @RequestMapping("/zookeeper/delete")
    @ResponseBody
    public JsonData<String> deleteZookeeper(String id) {
        JsonData<String> data = new JsonData<String>();

        zookeeperManager.deleteZookeeper(id);
        data.setData("Zookeeper配置删除成功！");

        return data;
    }


    @RequestMapping("dubbo")
    public String dubboIndex(ModelMap modelMap) {

        List<ZookeeperAddress> list = zookeeperManager.getAll();
        modelMap.put("zkList", list);

        Map<String, String> map = dubboManager.getAllDubboConfig();
        List<DubboProvider> providers = new ArrayList<DubboProvider>();
        for(String key : map.keySet()) {
            providers.add(new DubboProvider(map.get(key)));
        }
        modelMap.put("dubboList", providers);

        return "config/dubbo";
    }


    @RequestMapping("/dubbo/query")
    @ResponseBody
    public JsonData<List<DubboProvider>> queryDubbo(String interfaceClass, String zkId) {
        JsonData<List<DubboProvider>> jsonData = new JsonData<List<DubboProvider>>();

        try {

            if(StringUtils.isEmpty(interfaceClass)
                    || StringUtils.isEmpty(zkId)) {
                jsonData.setSuccess(false);
                jsonData.setError("请求参数为空！");
            }


            List<DubboProvider> list = zookeeperManager.getDubboProvider(interfaceClass, zkId);
            jsonData.setData(list);

        } catch (Exception e) {
            jsonData.setSuccess(false);
            jsonData.setError(e.getMessage());
            LOGGER.error("查询Dubbo服务异常！", e);
        }

        return jsonData;
    }

    @RequestMapping("/dubbo/save")
    @ResponseBody
    public JsonData<String> saveDubbo(String zkId, String dubboUrl) {
        JsonData<String> data = new JsonData<String>();

        if(StringUtils.isBlank(zkId) || StringUtils.isBlank(dubboUrl)) {
            data.setSuccess(false);
            data.setError("请求参数为空！");
            return data;
        }

        String zookeeperAddress = zookeeperManager.getAddressById(zkId);

        if(StringUtils.isEmpty(zookeeperAddress)) {
            data.setSuccess(false);
            data.setError("Zookeeper地址不存在！");
            return data;
        }

        try {
            dubboManager.addDubboServer(dubboUrl, zookeeperAddress);
            data.setData("Dubbo服务添加成功！");
        } catch (ClassNotFoundException e) {
            data.setSuccess(false);
            data.setError("接口类未能找到，请在lib目录下添加jar包重启后在试！");
        }

        return data;
    }

    @RequestMapping("/dubbo/delete")
    @ResponseBody
    public JsonData<String> deleteDubbo(String dubboUrl) {
        JsonData<String> data = new JsonData<String>();

        dubboManager.delete(dubboUrl);
        data.setData("Dubbo服务配置删除成功！");

        return data;
    }

}
