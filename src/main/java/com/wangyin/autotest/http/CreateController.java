package com.wangyin.autotest.http;

import com.alibaba.fastjson.JSON;
import com.wangyin.autotest.dto.DubboProvider;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.commons.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Case创建的controller.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
@RequestMapping("/create")
public class CreateController {

    private static final Logger LOGGER = new Logger();

    @Autowired
    private DubboManager dubboManager;

    @RequestMapping("/dubbo")
    public String dubbo(ModelMap modelMap, String serverUrl) {

        Map<String, String> map = dubboManager.getAllDubboConfig();
        List<DubboProvider> providers = new ArrayList<DubboProvider>();
        for(String key : map.keySet()) {
            providers.add(new DubboProvider(map.get(key)));
        }
        modelMap.put("list", providers);

        String json = JSON.toJSONString(providers);
        modelMap.put("json", json);

        modelMap.put("serverUrl", serverUrl);

        return "case/dubboCase";
    }

    @RequestMapping("/http")
    public String http() {
        return "case/httpCase";
    }

}
