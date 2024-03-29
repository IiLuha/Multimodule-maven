package com.itdev.aop.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonAspect {

    @Pointcut("within(com.*.service.*Service)")
    public void isServiceLayer() {

    }
}
