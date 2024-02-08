package org.ylab.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Action;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.implementations.OperationRepo;
import org.ylab.services.OperationService;

import java.time.LocalDateTime;

@Aspect
public class RecordableAspect {
    private final OperationUseCase operationUseCase;

    public RecordableAspect() {
        this.operationUseCase = new OperationService(
                new OperationRepo(
                        new ConnectionAdapter()));
    }

    @Pointcut("within(@org.ylab.aop.annotations.Recordable *) && execution(* *(..))")
    public void annotatedByRecordable() {

    }

    @AfterReturning("annotatedByRecordable()")
    public void recording(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for(Object arg : args) {
                if(arg instanceof Person person) {
                    operationUseCase.save(new Operation(person.getId(),
                            Action.AUTHENTICATION, LocalDateTime.now()));
                }
            }
        }

    }

}
