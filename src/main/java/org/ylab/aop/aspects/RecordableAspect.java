package org.ylab.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ylab.domain.enums.Role;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.enums.Action;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.domain.usecases.PersonUseCase;
import org.ylab.exceptions.ActionNotFoundException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.implementations.OperationRepo;
import org.ylab.repositories.implementations.PersonRepo;
import org.ylab.services.OperationService;
import org.ylab.services.PasswordService;
import org.ylab.services.PersonService;

import java.time.LocalDateTime;

/**
 * Аспект аудита
 */
@Aspect
public class RecordableAspect {
    private final OperationUseCase operationUseCase;

    private final PersonUseCase personUseCase;
    private final ConnectionAdapter connectionAdapter;
    private final PersonRepo personRepo;

    /**
     * Конструктор
     */
    public RecordableAspect() {
        connectionAdapter = new ConnectionAdapter();
        this.operationUseCase = new OperationService(
                new OperationRepo(
                        connectionAdapter));
        personRepo = new PersonRepo(connectionAdapter);
        personUseCase = new PersonService(new PasswordService(),
                personRepo);
    }

    /**
     * Совет
     * @param joinPoint выполняющийся метод
     * @return returning value of joinPoint
     * @throws Throwable
     */
    @Around("@annotation(org.ylab.aop.annotations.Recordable)")
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
