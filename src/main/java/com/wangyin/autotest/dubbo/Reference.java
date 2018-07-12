package com.wangyin.autotest.dubbo;

import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import org.springframework.context.ApplicationContext;

/**
 * TODO.
 *
 * @author chenyun313@gmail.com, 2015-03-19.
 * @version 1.0
 * @since 1.0
 */
public class Reference {

    public <T> ReferenceBean<T> init(String id, Class<T> interfaceCls, String version, String group,
                              RegistryConfig registry, ApplicationContext context) throws Exception {
        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setId(id);
        referenceBean.setApplicationContext(context);
        referenceBean.setGroup(group);
        referenceBean.setRegistry(registry);
        referenceBean.setInterface(interfaceCls);
        referenceBean.setVersion(version);

        referenceBean.afterPropertiesSet();

        return referenceBean;
    }

}
