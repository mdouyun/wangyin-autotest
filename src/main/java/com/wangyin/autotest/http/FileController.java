package com.wangyin.autotest.http;

import com.wangyin.autotest.dto.CaseInfo;
import com.wangyin.autotest.dto.JsonData;
import com.wangyin.autotest.manager.DubboManager;
import com.wangyin.autotest.manager.FileManager;
import com.wangyin.commons.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * case文件管理相关的controller.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
@RequestMapping("/file")
public class FileController {

    private static final Logger LOGGER = new Logger();

    @Autowired
    private FileManager fileManager;

    @RequestMapping("/index")
    public String index(String path, ModelMap modelMap) {

        modelMap.put("path", path);

        List<CaseInfo> list = fileManager.list(path);

        if(list != null) {

            List<CaseInfo> dirs = new ArrayList<CaseInfo>();
            List<CaseInfo> cases = new ArrayList<CaseInfo>();

            for(CaseInfo caseInfo : list) {

                if(caseInfo.isDir()) {
                    dirs.add(caseInfo);
                } else {
                    cases.add(caseInfo);
                }
            }

            modelMap.put("dirs", dirs);
            modelMap.put("cases", cases);
            modelMap.put("lastPath", fileManager.getParentPath(path));
        }

        return "file/file";
    }

    @RequestMapping("/case/delete")
    @ResponseBody
    public JsonData<String> deleteCase(String path) {
        JsonData<String> jsonData = new JsonData<String>();

        jsonData.setSuccess(fileManager.deleteCase(path));

        if(jsonData.isSuccess()) {
            jsonData.setData("Case文件删除成功！");
        } else {
            jsonData.setError("Case文件删除失败，请检查文件是否存在后再试！");
        }

        return jsonData;
    }

    @RequestMapping("/dir/delete")
    @ResponseBody
    public JsonData<String> deleteDir(String path) {
        JsonData<String> jsonData = new JsonData<String>();

        jsonData.setSuccess(fileManager.deleteCaseDir(path));

        if(jsonData.isSuccess()) {
            jsonData.setData("Case目录删除成功！");
        } else {
            jsonData.setError("Case目录删除失败，请检查目录是否存在文件后再试！");
        }

        return jsonData;
    }

    @RequestMapping("/dir/create")
    @ResponseBody
    public JsonData<String> createDir(String path, String dirName) {
        JsonData<String> jsonData = new JsonData<String>();

        jsonData.setSuccess(fileManager.createCaseDir(path, dirName));

        if(jsonData.isSuccess()) {
            jsonData.setData("Case目录新建成功！");
        } else {
            jsonData.setError("Case目录新建失败！");
        }

        return jsonData;
    }




}
