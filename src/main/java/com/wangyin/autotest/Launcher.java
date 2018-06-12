package com.wangyin.autotest;

import com.wangyin.commons.util.Logger;
import org.apache.jasper.servlet.JspServlet;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * WEB服务启动器.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class Launcher {

    private static final Logger LOGGER = new Logger();

    public static void main(String[] args) throws Exception {

        long time = System.currentTimeMillis();
        LOGGER.info("web服务开始启动......");


        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource webXml = resourceLoader.getResource("classpath:web.xml");
        File p = webXml.getFile().getParentFile();
        File webapp = new File(p.getAbsolutePath() + "/webapp");

        if(!webapp.exists()) {
            throw new FileNotFoundException("webapp目录不存在");
        }

        int port = 8889;

        if(args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        final Server server = new Server(port);
        server.setStopAtShutdown(true);

        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        wac.setDescriptor(webXml.getFile().getAbsolutePath());
        wac.setResourceBase(webapp.getAbsolutePath());
        wac.setParentLoaderPriority(true);

        ServletHolder holderJsp = new ServletHolder(JspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("development", "true");
        holderJsp.setInitParameter("modificationTestInterval", "30");
        holderJsp.setInitParameter("reload-interval", "30");
        wac.addServlet(holderJsp, "*.jsp");

        server.setHandler(wac);



        // 添加ThreadPool
        /*QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
        queuedThreadPool.setName("queuedTreadPool");
        queuedThreadPool.setMinThreads(10);
        queuedThreadPool.setMaxThreads(200);

        server.setThreadPool(queuedThreadPool);*/

        // 添加Connector
        /*SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8888);
        connector.setAcceptors(4);
        connector.setMaxIdleTime(10000);

        server.addConnector(connector);*/


        server.start();

        LOGGER.info("web服务启动完成，启动耗时:", System.currentTimeMillis() - time,
                "ms，访问地址：http://localhost:", port);

        server.join();
    }

}
