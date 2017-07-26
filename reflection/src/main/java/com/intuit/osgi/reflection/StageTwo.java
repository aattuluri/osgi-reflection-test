package com.intuit.osgi.reflection;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * Created by aattuluri on 7/23/17.
 */

@Component
public class StageTwo implements StageSync, Comparable<StageSync> {

    private static final Logger logger = LogManager.getLogger(StageTwo.class.getCanonicalName());

    @Autowired
    private GetToken validationClass;

    private final Random random = new Random();

    @Override
    public void applyMethod(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            IOUtils.toByteArray(request.getInputStream());
            //some IO & CPU
            String validateResponse = validationClass.getToken(null);
            response.setStatus(200);
            response.getOutputStream().write(validateResponse.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }

        if(random.nextInt() % 2 == 0) {
            throw new Exception("e");
        }
    }

    @Override
    public Integer getPriority() {
        return 2;
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
