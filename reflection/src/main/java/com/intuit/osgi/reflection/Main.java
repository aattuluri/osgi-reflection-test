package com.intuit.osgi.reflection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.logging.Logger;

/**
 * Created by aattuluri on 7/23/17.
 */
public class Main implements BundleActivator {

    private static final Logger logger = Logger.getLogger(Main.class.getCanonicalName());

    private ClassPathXmlApplicationContext applicationContext;

    public void start(BundleContext bundleContext) throws Exception {

        logger.info("Intuit reflection test bundle Started!");

        String file[] = {"beans/osgiApplicationContext.xml"};

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        GenericApplicationContext osgiBeanContext = new GenericApplicationContext();

        osgiBeanContext.refresh();

        applicationContext = new ClassPathXmlApplicationContext(file, false, osgiBeanContext);

        applicationContext.refresh ();

        Thread.currentThread().setContextClassLoader(cl);

        logger.info("Intuit reflection test bundle work done!");
    }

    public void stop(BundleContext context) throws Exception {
        logger.info ("Intuit reflection test bundle has been shutdown!!");
    }
}
