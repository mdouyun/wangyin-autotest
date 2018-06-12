package com.wangyin.autotest.http;

import com.alibaba.fastjson.JSON;
import com.wangyin.autotest.dto.*;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.autotest.manager.FileManager;
import com.wangyin.autotest.manager.ObjectManager;
import com.wangyin.commons.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 不区分case的一些请求处理.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
@RequestMapping("/case")
public class CaseController {

    @Autowired
    private DubboManager dubboManager;

    @Autowired
    private FileManager fileManager;

    private static final Logger LOGGER = new Logger();

    @RequestMapping("/edit")
    public String edit(String path, ModelMap modelMap) {

        try {
            CaseFile caseFile = fileManager.getCase(path);

            if(caseFile == null) {
                modelMap.put("error", "Case文件不存在！");
            } else {

                caseFile.setPath(StringUtils.removeEndIgnoreCase(caseFile.getPath(), ".xml"));

                modelMap.put("case", caseFile);

                if(caseFile instanceof DubboCaseFile) {
                    DubboProvider provider = new DubboProvider(caseFile.getUrl());
                    modelMap.put("provider", provider);
                } else if(caseFile instanceof HttpCaseFile) {
                    return "case/httpCaseEdit";
                }

            }

        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            LOGGER.error("编辑case异常！", e);
        }

        return "case/dubboCaseEdit";
    }

    @RequestMapping("/run")
    @ResponseBody
    public JsonData<Boolean> runCase(String path) {
        JsonData<Boolean> jsonData = new JsonData<Boolean>();

        try {
            CaseFile caseFile = fileManager.getCase(path);

            if(caseFile == null) {
                jsonData.setSuccess(false);
                jsonData.setError("Case未能找到！");
                return jsonData;
            } else if(caseFile instanceof DubboCaseFile) {

                Object[] args = ObjectManager.toObject(caseFile.getRequest());

                DubboProvider dubboProvider = new DubboProvider(caseFile.getUrl());

                Class cls = Class.forName(dubboProvider.getInterfaceName());

                Method[] methods = cls.getMethods();
                Method findMethod = null;
                for(Method method : methods) {

                    Class[] argTypes = method.getParameterTypes();
                    if(StringUtils.equals(method.getName(), ((DubboCaseFile) caseFile).getMethod())
                            && argTypes.length == args.length) {
                        findMethod = method;
                        break;
                    }
                }

                if(findMethod == null) {
                    jsonData.setSuccess(false);
                    jsonData.setError("未能找到接口中的方法！");
                    return jsonData;
                }

                // to send data
                Object provider = dubboManager.getDubboProvider(dubboProvider);

                Object response = findMethod.invoke(provider, args);
                String responseJson = JSON.toJSONString(response);

                caseFile.setResponse(responseJson);
                fileManager.storeCase(caseFile, path);

                String flagTrim = StringUtils.remove(caseFile.getCheckFlag(), ' ');
                responseJson = StringUtils.remove(responseJson, ' ');

                jsonData.setSuccess(true);
                if(StringUtils.isBlank(flagTrim) || StringUtils.contains(responseJson, flagTrim)) {
                    jsonData.setData(true);
                } else {
                    jsonData.setData(false);
                }
            }
        } catch (IOException e) {
            jsonData.setSuccess(false);
            jsonData.setError("IO异常：文件未找到");

            LOGGER.error("后台执行Case请求异常", e);
        } catch (ClassNotFoundException e) {
            jsonData.setSuccess(false);
            jsonData.setError("类异常：未找到对应的Class");

            LOGGER.error("后台执行Case请求异常", e);
        } catch (InvocationTargetException e) {
            jsonData.setSuccess(false);
            jsonData.setError("反射异常：属性或者方法反射有问题");

            LOGGER.error("后台执行Case请求异常", e);
        } catch (DocumentException e) {
            jsonData.setSuccess(false);
            jsonData.setError("Xml异常：Xml无法解析");

            LOGGER.error("后台执行Case请求异常", e);
        } catch (IllegalAccessException e) {
            jsonData.setSuccess(false);
            jsonData.setError("方法调用异常：输入或者输出参数有问题");

            LOGGER.error("后台执行Case请求异常", e);
        }

        return jsonData;
    }


}
