/*
 * @(#)DubboBeanFactory  1.0 2015-03-19
 *
 * Copyright 2009 chinabank payment All Rights Reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Author Email: yfchenyun@jd.com
 */
package com.wangyin.autotest.dubbo;


import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO.
 *
 * @author chenyun313@gmail.com, 2015-03-19.
 * @version 1.0
 * @since 1.0
 */
public class DubboBeanFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ReentrantLock registryLock = new ReentrantLock();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object registry(Class interfaceCls, String version, String group, String zkAddress) {

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
                this.applicationContext.getAutowireCapableBeanFactory();

        String zkBeanId = "dubbo-zk-" + zkAddress;

        if(!beanFactory.containsBean(zkBeanId)) {
            try {
                registryLock.lock();

                if(!beanFactory.containsBean(zkBeanId)) {
                    BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(RegistryConfig.class);
                    bdb.setLazyInit(false);
                    bdb.addPropertyValue("address", zkAddress);
                    beanFactory.registerBeanDefinition(zkBeanId, bdb.getBeanDefinition());
                }
            } finally {
                registryLock.unlock();
            }
        }

        RegistryConfig registryConfig = (RegistryConfig) this.applicationContext.getBean(zkBeanId);

        StringBuilder dubboBeanId = new StringBuilder("dubbo-client-");
        dubboBeanId.append(interfaceCls.getName()).append("&version=");
        dubboBeanId.append(version).append("&group=").append(group);


        if(!beanFactory.containsBean(dubboBeanId.toString())) {

            try {
                registryLock.lock();

                if(!beanFactory.containsBean(dubboBeanId.toString())) {

                    BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ReferenceBean.class);
                    bdb.setLazyInit(false);
                    bdb.addPropertyValue("registry", registryConfig);
                    bdb.addPropertyValue("group", group);
                    bdb.addPropertyValue("version", version);
                    bdb.addPropertyValue("interfaceClass", interfaceCls);
                    bdb.addPropertyValue("applicationContext", this.applicationContext);

                    beanFactory.registerBeanDefinition(dubboBeanId.toString(), bdb.getBeanDefinition());
                }
            } finally {
                registryLock.unlock();
            }
        }

        return this.applicationContext.getBean(dubboBeanId.toString());
    }

}
