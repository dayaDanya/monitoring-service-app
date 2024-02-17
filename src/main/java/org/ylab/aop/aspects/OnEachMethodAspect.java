package org.ylab.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *  Аспект логгирования
 */
@Component
@Aspect
public class OnEachMethodAspect {
    /**
     * Срез - при выполнении любого метода
     */
    @Pointcut("execution(* *(..))")
    public void eachMethod() {

    }

    /**
     * Совет
     * @param proceedingJoinPoint выполняющийся метод
     * @return результат joinPoint
     * @throws Throwable
     */
    @Around("eachMethod()")
    public Object recording(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }

}
