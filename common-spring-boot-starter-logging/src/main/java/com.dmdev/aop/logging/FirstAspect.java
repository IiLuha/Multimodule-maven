package com.dmdev.aop.logging;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FirstAspect {

    @Pointcut("within(com.*.service.*Service)")
    public void isServiceLayer() {

    }
}
