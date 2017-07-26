package com.intuit.osgi.reflection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by aattuluri on 7/23/17.
 */

@Component
public class StageOne implements StageSync, Comparable<StageSync> {

    private static final Logger logger = LogManager.getLogger(StageOne.class.getCanonicalName());

    @Autowired
    GetToken validationClass;

    @Override
    public void applyMethod(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //some CPU
            int i = 0;
            while(i < 2000) {
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getPriority() {
        return 1;
    }

    @Override
    public int compareTo(StageSync o) {
        return this.getPriority().compareTo(o.getPriority());
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
