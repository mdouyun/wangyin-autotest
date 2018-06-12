package com.wangyin.autotest.dto;

/**
 * http的case文件.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class HttpCaseFile extends CaseFile {

    private String method;

    private String header;

    private String mimeType;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
