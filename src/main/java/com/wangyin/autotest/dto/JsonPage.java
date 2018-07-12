package com.wangyin.autotest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyUi的分页数据格式.
 *
 * @author chenyun313@gmail.com, 2014-06-27.
 * @version 1.0
 * @since 1.0
 */
public class JsonPage<T> {

    private long total;

    private List<T> rows = new ArrayList<T>();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
