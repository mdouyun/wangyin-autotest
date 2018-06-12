/*
 * @(#)DataManager  1.0 2015-03-25
 *
 * Copyright 2009 chinabank payment All Rights Reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Author Email: yfchenyun@jd.com
 */
package com.wangyin.autotest.manager;

import com.wangyin.autotest.dto.DubboCaseFile;
import com.wangyin.autotest.dto.HttpCaseFile;
import com.wangyin.commons.util.Logger;
import com.wangyin.autotest.dto.CaseFile;
import com.wangyin.autotest.dto.CaseInfo;
import com.wangyin.autotest.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 文件操作主类.
 *
 * @author chenyun313@gmail.com, 2015-03-25.
 * @version 1.0
 * @since 1.0
 */
public class FileManager {

    private static final Logger LOGGER = new Logger();

    private static File USER_DIR;

    private static File CASE_DIR;

    private static File ZOOKEEPER_CONF;

    private static File DUBBO_CONF;

    private static File CONF_LOG;

    private static final Map<String, ReentrantLock> LOCK_MAP = new Hashtable<String, ReentrantLock>();

    private static final ReentrantLock LOCK = new ReentrantLock();

    private String userHome;

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public void init() {

        if(StringUtils.isBlank(userHome)) {
            userHome = System.getProperty("user.home");
            userHome = userHome + File.separator + ".knife";
        }

        USER_DIR = new File(userHome);
        if(!USER_DIR.exists() || !USER_DIR.isDirectory()) {
            USER_DIR.mkdirs();
        }

        // zk
        ZOOKEEPER_CONF = new File(USER_DIR, "zookeeper.conf");
        if(!ZOOKEEPER_CONF.exists()) {
            try {
                ZOOKEEPER_CONF.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Create zookeeper config file failure", e);
            }
        }

        // dubbo
        DUBBO_CONF = new File(USER_DIR, "dubbo.conf");
        if(!DUBBO_CONF.exists()) {
            try {
                DUBBO_CONF.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Create dubbo config file failure", e);
            }
        }

        // log
        CONF_LOG = new File(USER_DIR, "log.dat");
        if(!CONF_LOG.exists()) {
            try {
                CONF_LOG.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Create log file failure", e);
            }
        }

        // case
        CASE_DIR = new File(USER_DIR, "case");
        if(!CASE_DIR.exists() || !CASE_DIR.isDirectory()) {
            CASE_DIR.mkdirs();
        }

        LOGGER.info("Data dir path:：[", USER_DIR.getAbsolutePath(), "]");
    }

    public void storeDubboConf(Map<String, String> data) throws IOException {
        this.storeConf(data, DUBBO_CONF);
    }

    public void storeZookeeperConf(Map<String, String> data) throws IOException {
        this.storeConf(data, ZOOKEEPER_CONF);
    }

    private void storeConf(Map<String, String> data, File file) throws IOException {

        try {

            LOCK.lock();

            String oldContent = IOUtils.readFile(file);

            if(StringUtils.isNotBlank(oldContent)) {
                Date sysdate = new Date();
                StringBuilder rowSplit = new StringBuilder();
                rowSplit.append("\n******************************").append(sysdate.toString());
                rowSplit.append("******************************").append("\n");

                IOUtils.writeFile(rowSplit.toString(), CONF_LOG, true);
                IOUtils.writeFile(oldContent, CONF_LOG, true);
                IOUtils.writeFile(rowSplit.toString(), CONF_LOG, true);
            }

            IOUtils.storeProperties(data, file);
        } finally {
            LOCK.unlock();
        }
    }

    public Map<String, String> getZookeeperConf() throws IOException {
        return getConf(ZOOKEEPER_CONF);
    }

    public Map<String, String> getDubboConf() throws IOException {
        return getConf(DUBBO_CONF);
    }

    private Map<String, String> getConf(File file) throws IOException {
        Map map = IOUtils.readProperties(file);
        return map;
    }

    public CaseFile getCase(String path) throws IOException, DocumentException {
        File file = new File(CASE_DIR, path);

        if(!file.exists()) {
            throw new FileNotFoundException(path + "is not fount!");
        }

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);

        Element root = document.getRootElement();

        CaseFile caseFile = null;

        Element config = root.element("configuration");

        String type = config.attributeValue("type");

        if(StringUtils.equals(type, "http")) {
            caseFile = new HttpCaseFile();

            ((HttpCaseFile) caseFile).setMethod(config.element("method").getText());
            ((HttpCaseFile) caseFile).setHeader(config.element("header").getText());
            ((HttpCaseFile) caseFile).setMimeType(config.element("mimeType").getText());
        } else {
            caseFile = new DubboCaseFile();
            ((DubboCaseFile) caseFile).setZookeeperAddress(config.element("zookeeper").getText());
            ((DubboCaseFile) caseFile).setMethod(config.element("method").getText());
        }

        caseFile.setUrl(config.element("url").getText());

        Element request = root.element("request");
        caseFile.setRequest(request.getText());

        Element response = root.element("response");
        caseFile.setResponse(response.getText());
        caseFile.setCheckFlag(response.attributeValue("check"));

        caseFile.setName(file.getName());
        caseFile.setPath(StringUtils.replace(path, "\\", "/"));
        caseFile.setParentPath(file.getParent());

        return caseFile;
    }

    public void storeCase(CaseFile caseFile, String path) throws IOException {
        File file = new File(CASE_DIR, path);

        File pFile = file.getParentFile();
        if(!pFile.exists()) {
            pFile.mkdirs();
        }

        if(!file.exists()) {
            file.createNewFile();
        }

        Element root = DocumentHelper.createElement("auto-case");
        Document document = DocumentHelper.createDocument(root);

        Element config = root.addElement("configuration");
        config.addElement("url").setText(caseFile.getUrl());

        if(caseFile instanceof HttpCaseFile) {

            config.addAttribute("type", "http");

            config.addElement("method").setText(((HttpCaseFile) caseFile).getMethod());
            config.addElement("header").setText(((HttpCaseFile) caseFile).getHeader());
            config.addElement("mimeType").setText(((HttpCaseFile) caseFile).getMimeType());

        } else if(caseFile instanceof DubboCaseFile) {

            config.addAttribute("type", "dubbo");

            config.addElement("method").setText(((DubboCaseFile) caseFile).getMethod());
            config.addElement("zookeeper").setText(((DubboCaseFile) caseFile).getZookeeperAddress());
        }

        root.addElement("request").setText(caseFile.getRequest());

        Element response = root.addElement("response");
        response.setText(caseFile.getResponse());
        response.addAttribute("check", caseFile.getCheckFlag());

        // 设置缩进为4个空格，并且另起一行为true
        OutputFormat format = new OutputFormat("    ", true);

        String key = file.getAbsolutePath();
        ReentrantLock lock = LOCK_MAP.get(key);
        if(lock == null) {
            try {
                LOCK.lock();

                if(lock == null) {
                    lock = new ReentrantLock();
                    LOCK_MAP.put(key, lock);
                }
            } finally {
                LOCK.unlock();
            }
        }

        try {
            lock.lock();

            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), format);
            xmlWriter.write(document);

            xmlWriter.close();

            caseFile.setName(file.getName());
            caseFile.setPath(file.getAbsolutePath());
            caseFile.setParentPath(file.getParent());
        } finally {
            lock.unlock();
        }
    }

    public String getParentPath(String path) {

        File dir = CASE_DIR;

        if(StringUtils.isNotEmpty(path)) {
            dir = new File(CASE_DIR, path);
        }

        if(!dir.exists() || !dir.isDirectory()
                || StringUtils.equals(dir.getAbsolutePath(), CASE_DIR.getAbsolutePath())) {
            return null;
        }

        String p = dir.getParent();

        p = StringUtils.remove(p, CASE_DIR.getAbsolutePath());
        p = StringUtils.replace(p, "\\", "/");

        return p;
    }

    public boolean deleteCase(String path) {
        File caseFile = CASE_DIR;

        if(StringUtils.isNotEmpty(path)) {
            caseFile = new File(CASE_DIR, path);
        }

        if(!caseFile.exists() || caseFile.isDirectory()) {
            return false;
        }

        return caseFile.delete();
    }

    public boolean deleteCaseDir(String path) {
        File dir = CASE_DIR;

        if(StringUtils.isNotEmpty(path)) {
            dir = new File(CASE_DIR, path);
        }

        if(!dir.exists() || !dir.isDirectory()
                || StringUtils.equals(dir.getAbsolutePath(), CASE_DIR.getAbsolutePath())) {
            return false;
        }

        return dir.delete();
    }

    public boolean createCaseDir(String path, String dirName) {
        File dir = CASE_DIR;

        if(StringUtils.isNotEmpty(path)) {
            dir = new File(CASE_DIR, path);
        }

        if(!dir.exists() || !dir.isDirectory()) {
            return false;
        }

        dir = new File(dir, dirName);
        return dir.mkdirs();
    }

    public List<CaseInfo> list(String path) {

        File dir = CASE_DIR;

        if(StringUtils.isNotEmpty(path)) {
            dir = new File(CASE_DIR, path);
        }

        if(!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        File[] files = dir.listFiles();

        List<CaseInfo> list = new ArrayList<CaseInfo>();

        for(File file : files) {

            CaseInfo caseInfo = new CaseInfo();

            caseInfo.setName(file.getName());

            Date date = new Date(file.lastModified());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            caseInfo.setModifiedTime(format.format(date));

            caseInfo.setPath(StringUtils.remove(file.getAbsolutePath(), CASE_DIR.getAbsolutePath()));
            caseInfo.setPath(StringUtils.replace(caseInfo.getPath(), "\\", "/"));

            if(file.isDirectory()) {
                caseInfo.setDir(true);
            } else {
                caseInfo.setDir(false);
            }

            list.add(caseInfo);
        }

        return list;
    }

}
