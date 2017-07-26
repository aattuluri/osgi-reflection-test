package com.intuit.osgi.reflection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;

import static com.intuit.osgi.reflection.Constants.SERVER_PORT;

/**
 * Created by aattuluri on 7/23/17.
 *
 *
 * Starts a jetty http server on port 10081
 *
 */

@Component
public class JettyServer {

    private static final Logger logger = LogManager.getLogger(JettyServer.class.getCanonicalName());

    private Integer maxThreadPool = 2000;

    private Integer minThreadPool = 10;

    private Server server;

    @Autowired
    RequestHandler requestHandler;

    @PostConstruct
    public void init() throws Exception {

        logger.info("Starting jetty...");

        server = new Server(new QueuedThreadPool(maxThreadPool, minThreadPool));

        ServerConnector restInterfaceConnector = new ServerConnector(server, getHttpConnectionFactory());

        restInterfaceConnector.setPort(SERVER_PORT);

        server.addConnector(restInterfaceConnector);

        Set<Handler> handlers = new HashSet<>();
        ContextHandler context = new ContextHandler();
        context.setAllowNullPathInfo(true);
        context.setContextPath("/");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setHandler(requestHandler);
        handlers.add(context);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(handlers.toArray(new Handler[]{}));

        server.setHandler(contexts);
        server.start();

        logger.info("Jetty http server has been started on port: " + SERVER_PORT);
    }

    @PreDestroy
    public void destroy() throws Exception {

        logger.info("Stopping jetty...");
        server.stop();
        logger.info("Stopped jetty");
    }

    private HttpConnectionFactory getHttpConnectionFactory() {
        HttpConfiguration validatorConfig = new HttpConfiguration();
        validatorConfig.setSendServerVersion(false);
        validatorConfig.setRequestHeaderSize(32768);
        validatorConfig.setResponseHeaderSize(32768);
        validatorConfig.setOutputBufferSize(32000);
        validatorConfig.setMaxErrorDispatches(0);

        return new HttpConnectionFactory(validatorConfig);
    }

}
