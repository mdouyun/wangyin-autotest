package com.wangyin.autotest.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangyin.autotest.dto.CaseFile;
import com.wangyin.autotest.dto.DubboCaseFile;
import com.wangyin.autotest.dto.DubboProvider;
import com.wangyin.autotest.dto.JsonData;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.autotest.manager.FileManager;
import com.wangyin.autotest.manager.ObjectManager;
import com.wangyin.commons.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Dubbo case相关的Controller，包含case的生成、测试及保存.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
@RequestMapping("/dubbo")
public class DubboCaseController {

    @Autowired
    private DubboManager dubboManager;

    @Autowired
    private FileManager fileManager;

    private static final Logger LOGGER = new Logger();


    @RequestMapping("/getRequestData")
    @ResponseBody
    public JsonData<String> getRequestData(String dubboUrl, String methodName) {
        JsonData<String> data = new JsonData<String>();

        try {
            DubboProvider dubboProvider = new DubboProvider(dubboUrl);

            Class cls = Class.forName(dubboProvider.getInterfaceName());

            Method[] methods = cls.getMethods();
            Method findMethod = null;
            for(Method method : methods) {
                if(StringUtils.equals(method.getName(), methodName)) {
                    findMethod = method;
                    break;
                }
            }

            if(findMethod == null) {
                data.setSuccess(false);
                data.setError("未能找到接口中的方法！");
                return data;
            }

            Class[] argTypes = findMethod.getParameterTypes();

            StringBuilder json = new StringBuilder("[");
            for(int i = 0; i < argTypes.length; i++) {
                json.append("{").append("\"参数-").append(i+1).append("\":");
                json.append(ObjectManager.toJSON(argTypes[i])).append("}");

                if(i < argTypes.length - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            data.setData(json.toString());
        } catch (Exception e) {
            data.setSuccess(false);
            data.setError(e.getMessage());

            LOGGER.error("根据Dubbo参数生成请求json异常！", e);
        }

        return data;
    }

    @RequestMapping("/sendRequest")
    @ResponseBody
    public JsonData<String> sendRequest(String dubboUrl, String methodName, String requestJson) {
        JsonData<String> data = new JsonData<String>();

        try {

            Object[] args = ObjectManager.toObject(requestJson);

            DubboProvider dubboProvider = new DubboProvider(dubboUrl);

            Class cls = Class.forName(dubboProvider.getInterfaceName());

            Method[] methods = cls.getMethods();
            Method findMethod = null;
            for(Method method : methods) {

                Class[] argTypes = method.getParameterTypes();
                if(StringUtils.equals(method.getName(), methodName)
                        && argTypes.length == args.length) {
                    findMethod = method;
                    break;
                }
            }

            if(findMethod == null) {
                data.setSuccess(false);
                data.setError("未能找到接口中的方法！");
                return data;
            }

            // to send data
            Object provider = dubboManager.getDubboProvider(dubboProvider);

            Object response = findMethod.invoke(provider, args);
            data.setData(JSON.toJSONString(response));

        } catch (Exception e) {
            data.setSuccess(false);
            data.setError(e.getMessage());

            LOGGER.error("发送Dubbo请求异常！", e);
        }

        return data;
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonData<String> save(String dubboUrl, String methodName,
                                          String requestJson, String responseJson,
                                          String checkFlag, String savePath) {
        JsonData<String> data = new JsonData<String>();

        try {
            DubboCaseFile caseFile = new DubboCaseFile();
            caseFile.setUrl(dubboUrl);
            caseFile.setMethod(methodName);
            caseFile.setZookeeperAddress(dubboManager.getZookeeperAddress(dubboUrl));

            caseFile.setRequest(requestJson);
            caseFile.setResponse(responseJson);
            caseFile.setCheckFlag(checkFlag);

            if(!StringUtils.endsWithIgnoreCase(savePath, ".xml")) {
                savePath = savePath + ".xml";
            }

            fileManager.storeCase(caseFile, savePath);

            data.setData("Dubbo Case保存成功！");
        } catch (IOException e) {
            data.setSuccess(false);
            data.setError(e.getMessage());

            LOGGER.error("Dubbo Case保存异常！", e);
        }

        return data;
    }

}
