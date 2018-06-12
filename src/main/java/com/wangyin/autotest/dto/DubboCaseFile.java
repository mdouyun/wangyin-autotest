package com.wangyin.autotest.dto;

/**
 * dubbo的case文件.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class DubboCaseFile extends CaseFile {

    private String method;

    private String zookeeperAddress;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getZookeeperAddress() {
        return zookeeperAddress;
    }

    public void setZookeeperAddress(String zookeeperAddress) {
        this.zookeeperAddress = zookeeperAddress;
    }

}
