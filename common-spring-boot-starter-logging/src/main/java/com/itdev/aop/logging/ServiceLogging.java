package com.itdev.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLogging {

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && @annotation(org.springframework.transaction.annotation.Transactional)")
    public void anyNotReadOnlyMethod() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public * findById(*))")
    public void anyFindByIdServiceMethod() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public * loadUserByUsername(*))")
    public void loadUserByUsernameServiceMethod() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public * findByFilter(*))")
    public void anyFindByFilterServiceMethod() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public * findAll(*,*))")
    public void anyFindAllServiceMethodReturnsPage() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public java.util.List findAll())")
    public void anyFindAllServiceMethodReturnsList() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public java.util.Optional findImage(*))")
    public void anyFindImageServiceMethod() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public void upload(*,*))")
    public void uploadImageServiceMethod() {
    }

    @Pointcut("com.itdev.aop.common.CommonAspect.isServiceLayer() && execution(public void download(*))")
    public void downloadImageServiceMethod() {
    }

    @Pointcut("anyNotReadOnlyMethod() && execution(public * create(*))")
    public void anyCreateServiceMethod() {
    }

    @Pointcut("anyNotReadOnlyMethod() && execution(public * update(*,*))")
    public void anyUpdateServiceMethod() {
    }

    @Pointcut("anyNotReadOnlyMethod() && execution(public * delete(*))")
    public void anyDeleteServiceMethod() {
    }

    @Around("anyFindByIdServiceMethod() " +
            "&& args(id) " +
            "&& target(service)")
    public Object addLoggingToFindById(ProceedingJoinPoint joinPoint, Object id, Object service) throws Throwable {
        log.info("Invoked findById method in class {} with id {}", service, id);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked findById method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked findById method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyFindAllServiceMethodReturnsPage() " +
            "&& args(filter, pageable) " +
            "&& target(service)")
    public Object addLoggingToFindAllReturnsPage(ProceedingJoinPoint joinPoint, Object filter, Object pageable, Object service) throws Throwable {
        log.info("Invoked findAllReturnsPage method in class {} with filter{} and pageable {}", service, filter, pageable);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked findAllReturnsPage method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked findAllReturnsPage method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyFindAllServiceMethodReturnsList() " +
            "&& target(service)")
    public Object addLoggingToFindAllReturnsList(ProceedingJoinPoint joinPoint, Object service) throws Throwable {
        log.info("Invoked findAllReturnsList method in class {} without params", service);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked findAllReturnsList method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked findAllReturnsList method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyFindImageServiceMethod() " +
            "&& args(id) " +
            "&& target(service)")
    public Object addLoggingToFindImage(ProceedingJoinPoint joinPoint, Object id, Object service) throws Throwable {
        log.info("Invoked findImage method in class {} with id {}", service, id);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked findImage method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked findImage method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("uploadImageServiceMethod() " +
            "&& args(imagePath, content) " +
            "&& target(service)")
    public Object addLoggingUpload(ProceedingJoinPoint joinPoint, Object imagePath, Object content, Object service) throws Throwable {
        log.info("Invoked upload method in class {} with imagePath {} and content {}", service, imagePath, content);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked upload method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked upload method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("downloadImageServiceMethod() " +
            "&& args(imagePath) " +
            "&& target(service)")
    public Object addLoggingToDownload(ProceedingJoinPoint joinPoint, Object imagePath, Object service) throws Throwable {
        log.info("Invoked download method in class {} with imagePath {}", service, imagePath);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked download method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked download method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyCreateServiceMethod() " +
            "&& args(dto) " +
            "&& target(service)")
    public Object addLoggingToCreate(ProceedingJoinPoint joinPoint, Object dto, Object service) throws Throwable {
        log.info("Invoked create method in class {} with dto {}", service, dto);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked create method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked create method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyUpdateServiceMethod() " +
            "&& args(id, dto) " +
            "&& target(service)")
    public Object addLoggingToUpdate(ProceedingJoinPoint joinPoint, Object id, Object dto, Object service) throws Throwable {
        log.info("Invoked update method in class {} with id {} and dto {}", service, id, dto);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked update method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked update method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyDeleteServiceMethod() " +
            "&& args(id) " +
            "&& target(service)")
    public Object addLoggingToDelete(ProceedingJoinPoint joinPoint, Object id, Object service) throws Throwable {
        log.info("Invoked delete method in class {} with id {}", service, id);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked delete method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked delete method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("anyFindByFilterServiceMethod() " +
            "&& args(filter) " +
            "&& target(service)")
    public Object addLoggingToFindFilter(ProceedingJoinPoint joinPoint, Object filter, Object service) throws Throwable {
        log.info("Invoked findFilter method in class {} with filter {}", service, filter);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked findFilter method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked findFilter method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }

    @Around("loadUserByUsernameServiceMethod() " +
            "&& args(username) " +
            "&& target(service)")
    public Object addLoggingToLoadUserByUsername(ProceedingJoinPoint joinPoint, Object username, Object service) throws Throwable {
        log.info("Invoked loadUserByUsername method in class {} with username {}", service, username);
        try {
            Object result = joinPoint.proceed();
            log.info("Invoked loadUserByUsername method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("Invoked loadUserByUsername method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        }
    }
}
