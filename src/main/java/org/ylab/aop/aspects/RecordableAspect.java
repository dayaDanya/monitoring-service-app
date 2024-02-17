package org.ylab.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ylab.domain.enums.Action;
import org.ylab.domain.enums.Role;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.exceptions.ActionNotFoundException;
import org.ylab.repositories.IPersonRepo;

import java.time.LocalDateTime;

/**
 * Аспект аудита
 */
@Component
@Aspect
public class RecordableAspect {
    private final OperationUseCase operationUseCase;

    private final IPersonRepo personRepo;

    /**
     * Конструктор
     */
    @Autowired
    public RecordableAspect(OperationUseCase operationUseCase,
                            IPersonRepo personRepo) {
        this.operationUseCase = operationUseCase;
        this.personRepo = personRepo;
    }

    @Pointcut("within(@org.ylab.aop.annotations.Recordable *) && execution(* *(..))")
    public void annotatedByRecordable() {

    }

    /**
     * Совет
     *
     * @param joinPoint выполняющийся метод
     * @return returning value of joinPoint
     * @throws Throwable
     */
    @Around("annotatedByRecordable()")
//    @Around("@annotation(org.ylab.aop.annotations.Recordable)")
    public Object recording(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        Action action = null;
        Object result = joinPoint.proceed();

        try {
            action = Action.find(name);
        } catch (ActionNotFoundException e) {
            return result;
        }
        long id = 0L;
        if (action == Action.GIVE_COUNTER || action == Action.ADD_NEW_COUNTER_TYPE
                || action == Action.WATCH_AUDIT || action == Action.WATCH_ALL)
            id = personRepo.findIdByRole(Role.ADMIN).get();
        else {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                System.out.println(arg.toString());
                if (arg instanceof Long) {
                    id = (long) arg;
                    break;
                } else if (arg instanceof Person person) {
                    id = personRepo.findIdByEmail(person.getEmail()).get();
                }

            }
        }
        operationUseCase.save(new Operation(id, action, LocalDateTime.now()));
        return result;
    }
}
