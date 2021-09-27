package com.epam.jwd.center.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import com.epam.jwd.center.exceptions.CallCenterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CallCenter {

    private static final int OPERATOR_AMOUNT = 3;
    private static final Logger logger = LogManager.getLogger(CallCenter.class);
    private static final ReentrantLock INSTANCE_LOCK = new ReentrantLock();
    private static final ReentrantLock OPERATOR_LOCK = new ReentrantLock();
    private static final AtomicBoolean IS_INSTANCE_CREATED = new AtomicBoolean(false);
    private final Semaphore semaphore = new Semaphore(OPERATOR_AMOUNT, false);
    private static CallCenter callCenterInstance;

    private final List<Operator> allOperators;
    private List<CallScope> scopes = Arrays.asList(CallScope.TECH, CallScope.ORDERS, CallScope.SUPPORT);

    private CallCenter() {
        logger.info("Beginning of work the CALL CENTER \n");
        allOperators = new ArrayList<>();
        for (int i = 0; i < OPERATOR_AMOUNT; i++) {
            Random random = new Random();
            int min = 5;
            int max = 100;
            int diff = max - min;
            int operatorId = random.nextInt(diff + 1) + min;
            Operator operator = new Operator(operatorId, scopes.get(i));
            logger.info("Operator went to work \n" + operator);
            allOperators.add(operator);
        }
        logger.info("All operators went to work \n");
    }

    public static CallCenter getInstance() {
        if (!IS_INSTANCE_CREATED.get()) {
            INSTANCE_LOCK.lock();
            try {
                if (!IS_INSTANCE_CREATED.get()) {
                    callCenterInstance = new CallCenter();
                    logger.info("Call center is created");
                    IS_INSTANCE_CREATED.set(true);
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }
        return callCenterInstance;
    }

    public Operator acquireOperator(Client client) throws CallCenterException {
        try {
            semaphore.acquire();
            logger.info(String.format("CALL CENTER: %s is accepted on the issue %s \n", client.getName() ,client.getCallScope()));
            OPERATOR_LOCK.lock();
            Operator operator = null;
            int indexOperator = findRequiredOperator(client);
            if (indexOperator != -1) {
                operator = allOperators.remove(indexOperator);
                logger.info(String.format("CALL CENTER: Operator on the issue " +
                        "%s with id %d in touch with" +
                        " a client by name - %s " +
                        "on the client issue - %s \n", operator.getCallScope(),operator.getId(),
                        client.getName() ,client.getCallScope()));
            } else {
                logger.info(String.format("CALL CENTER: Dear %s the operator %s you need " +
                                "is busy. Please wait \n", client.getName(), client.getCallScope()));
                semaphore.release();

            }
            return operator;
        } catch (InterruptedException e) {
            throw new CallCenterException(e.getMessage(), e);
        } finally {
            OPERATOR_LOCK.unlock();
        }
    }

    public void releaseOperator(Operator operator) {
        OPERATOR_LOCK.lock();
        try {
            allOperators.add(operator);
            semaphore.release();
            logger.info(String.format("CALL CENTER: Operator #%d on the issue %s is free \n",
                            operator.getId(),operator.getCallScope()));
        } finally {
            OPERATOR_LOCK.unlock();
        }
    }

    public int findRequiredOperator(Client client) {
        int index = -1;
        for (int i = 0; i < allOperators.size(); i++) {
            if (client.getCallScope() == allOperators.get(i).getCallScope()) {
                index = i;
            }
        }
        return index;
    }
}