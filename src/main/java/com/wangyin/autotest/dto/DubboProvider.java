package com.wangyin.autotest.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * dubbo的服务提供者要素.
 *
 * @author chenyun313@gmail.com, 2015-03-19.
 * @version 1.0
 * @since 1.0
 */
public class DubboProvider {

    private String dubboUrl;

    private String version;

    private String group;

    private String interfaceName;

    private String methods;

    private String serverUrl;

    private String serverIp;

    public String getServerUrl() {
        return serverUrl;
    }

    public String getMethods() {
        return methods;
    }

    public String getDubboUrl() {
        return dubboUrl;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getInterfaceSimpleName() {
        String[] array = StringUtils.split(interfaceName, ".");

        StringBuilder name = new StringBuilder();
        for(int i = 0; i < array.length; i++) {

            if(i < array.length - 1) {
                name.append(StringUtils.left(array[i], 1));
                name.append(".");
            } else {
                name.append(array[i]);
            }
        }

        return name.toString();
    }

    public DubboProvider(String dubboUrl) {
        this.dubboUrl = dubboUrl;

        int index = StringUtils.indexOf(dubboUrl, '?');
        this.serverUrl = StringUtils.substring(dubboUrl, 0, index);
        String parameter = StringUtils.substring(dubboUrl, index);
        String[] parameters = StringUtils.split(parameter, '&');

        Map<String, String> map = new HashMap<String, String>();

        for(String par : parameters) {

            if(StringUtils.isBlank(par)) {
                continue;
            }

            String[] kv = StringUtils.split(par, "=");
            if(kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }

        this.group = map.get("group");
        this.version = map.get("version");
        this.interfaceName = map.get("interface");
        this.methods = map.get("methods");
        this.serverIp = StringUtils.remove(this.serverUrl, this.interfaceName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DubboProvider{");
        sb.append("dubboUrl='").append(dubboUrl).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", interfaceName='").append(interfaceName).append('\'');
        sb.append(", methods='").append(methods).append('\'');
        sb.append(", serverUrl='").append(serverUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        DubboProvider provider = new DubboProvider("dubbo://172.24.5.145:20885/com.wangyin.npp.payment.facade.PaymentManageService?anyhost=true&application=payment-service-provider&default.retries=0&default.timeout=25000&dispather=message&dubbo=2.4.10.1-wy&interface=com.wangyin.npp.payment.facade.PaymentManageService&methods=queryTradePaymentInfo,paymentLookup,queryPaymentInfoByTradeNo&pid=10355&revision=2.1.2&side=provider&threads=600&timestamp=1439188040341&version=2.0.0");

        System.out.println(provider.getInterfaceSimpleName());

    }
}
