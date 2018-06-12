/*
 * @(#)JsonData  1.0 2014-06-26
 *
 * Copyright 2009 chinabank payment All Rights Reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Author Email: yfchenyun@jd.com
 */
package com.wangyin.autotest.dto;

/**
 * 通用的Ajax交互数据格式.
 *
 * @author chenyun313@gmail.com, 2014-06-26.
 * @version 1.0
 * @since 1.0
 */
public class JsonData<T> {

    private boolean success = true;

    private String error;

    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JsonData{");
        sb.append("success=").append(success);
        sb.append(", error='").append(error).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
