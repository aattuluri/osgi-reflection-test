package com.intuit.osgi.reflection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by aattuluri on 7/23/17.
 *
 * Synchronous stage
 *
 */

public interface StageSync extends Stage {

    void applyMethod(HttpServletRequest request, HttpServletResponse response) throws Exception;

    Integer getPriority();

}
