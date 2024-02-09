package org.ylab.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.enums.Action;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.domain.usecases.PersonUseCase;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.implementations.OperationRepo;
import org.ylab.repositories.implementations.PersonRepo;
import org.ylab.services.OperationService;
import org.ylab.services.PasswordService;
import org.ylab.services.PersonService;

import java.time.LocalDateTime;

@Aspect
public class RecordableAspect {
    private final OperationUseCase operationUseCase;

    private final PersonUseCase personUseCase;
    private final ConnectionAdapter connectionAdapter;
    private final PersonRepo personRepo;

    public RecordableAspect() {
        connectionAdapter = new ConnectionAdapter();
        this.operationUseCase = new OperationService(
                new OperationRepo(
                        connectionAdapter));
        personRepo = new PersonRepo(connectionAdapter);
        personUseCase = new PersonService(new PasswordService(),
                personRepo);
    }

    @Pointcut("within(@org.ylab.aop.annotations.Recordable *) && execution(* *(..))")
    public void annotatedByRecordable() {

    }

    @AfterReturning("annotatedByRecordable()")
    public void recording(JoinPoint joinPoint) {
        long id = 0L;
        String name = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                System.out.println(arg.toString());
                if (arg instanceof Long) {
                    id = (long) arg;
                    break;
                } else if (arg instanceof Person person) {
                    id = personRepo.findIdByEmail(person.getEmail()).get();
                }
            }
            Action action = Action.find(name);
            operationUseCase.save(new Operation(id, action, LocalDateTime.now()));
            //todo просмотреть исключения и зарефакторить
        }


    }

}
