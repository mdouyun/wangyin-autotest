package com.wangyin.autotest.dto;

/**
 * case缩略信息.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class CaseInfo {

    private String name;

    private boolean isDir;

    private String path;

    private String modifiedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
