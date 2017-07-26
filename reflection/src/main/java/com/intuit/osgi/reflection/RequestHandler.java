package com.intuit.osgi.reflection;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by aattuluri on 7/23/17.
 *
 * Handles all the incoming http requests
 */

@Component
public class RequestHandler extends AbstractHandler {

    private static final Logger logger = LogManager.getLogger(StageOne.class.getCanonicalName());

    @Autowired
    private Set<StageSync> reflectionClassSet;

    @PostConstruct
    public void init() {
        reflectionClassSet = reflectionClassSet.stream().sorted().collect(Collectors.toSet());
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        //if validate then just return the token
        if(request.getPathInfo().startsWith("/validate")) {
            IOUtils.toByteArray(request.getInputStream());
            response.setStatus(200);
            response.getOutputStream().write(UUID.randomUUID().toString().getBytes());
        } else {

            //apply stages in the pipeline
            try {
                Set<StageSync> stages = getReflectionClasses();
                for (StageSync stage : stages) {
                    stage.applyMethod(request, response);
                }
            } catch(Exception e) {
                logger.error("Exception occurred", e);
            }
        }

        response.flushBuffer();
    }

    private Set<StageSync> getReflectionClasses() {
        Set<StageSync> reflectionClasses = new HashSet<>();
        reflectionClasses.addAll(reflectionClassSet);
        return reflectionClasses.stream().sorted().collect(Collectors.toSet());
    }

}
