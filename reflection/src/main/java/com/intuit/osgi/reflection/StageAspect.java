package com.intuit.osgi.reflection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by aattuluri on 7/26/17.
 *
 * Intercepts all the stages in the pipeline
 *
 */
@Component
@Aspect
public class StageAspect {

    private static final Logger logger = LogManager.getLogger(StageAspect.class.getCanonicalName());

    @Around("execution(* com.intuit.osgi.reflection..applyMethod(..))")
    public Object profileSync(ProceedingJoinPoint joinPoint) throws Throwable {

        Stage stage = (Stage) joinPoint.getTarget();

        Object object = null;

        try {

            if (stage instanceof StageSync) {

                StageSync currentStage = (StageSync) stage;

                try {
                    logger.info("Proceeding with stage: " + currentStage.getName() + " with priority: " + currentStage.getPriority());
                    object = joinPoint.proceed();
                } catch (Exception e) {
                    logger.info("Throwing exception from stage: " + currentStage.getName() + " with priority: " + currentStage.getPriority());
                    throw e;

                }
            } else {
                object = joinPoint.proceed();
            }


        }finally {
            //all done
        }
        return object;
    }
}

