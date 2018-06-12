package com.wangyin.autotest.dto;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ZookeeperAddress {

    private String id;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id).append(':').append(address);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
