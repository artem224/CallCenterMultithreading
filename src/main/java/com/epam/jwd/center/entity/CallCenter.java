package com.epam.jwd.center.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class CallCenter {

    private static final int OPERATOR_AMOUNT = 3;


    private static final ReentrantLock INSTANCE_LOCK = new ReentrantLock();
    private static final ReentrantLock OPERATOR_LOCK = new ReentrantLock();
    private static final AtomicBoolean IS_INSTANCE_CREATED = new AtomicBoolean(false);
    private final Semaphore semaphore = new Semaphore(OPERATOR_AMOUNT, true);
    private static CallCenter callCenterInstance;

    private final List<Operator> allOperators;
    private List<CallScope> scopes = Arrays.asList(CallScope.TECH,CallScope.ORDERS,CallScope.SUPPORT);

    private CallCenter() {
        //LOGGER.info("Starting creating taxi...");
        allOperators = new ArrayList<>();
        for (int i = 0; i < OPERATOR_AMOUNT; i++) {
//            int taxiLocationX = (int) ((Math.random() * MAX_TAXI_LOCATION - MIN_TAXI_LOCATION + 1))
//                    + MIN_TAXI_LOCATION;
//            int taxiLocationY = (int) ((Math.random() * MAX_TAXI_LOCATION - MIN_TAXI_LOCATION + 1))
//                    + MIN_TAXI_LOCATION;
//            double taxiRadius = (Math.random() * MAX_TAXI_RADIUS - MIN_TAXI_RADIUS) + MIN_TAXI_RADIUS;
            Random random = new Random();
            int min = 5;
            int max = 100;
            int diff = max - min;
            int operatorId = random.nextInt(diff + 1) + min;
            Operator taxi = new Operator(operatorId,scopes.get(i));
            //LOGGER.info("Created taxi: " + taxi);
            allOperators.add(taxi);
        }
//        LOGGER.info("Taxi was created!");
    }

    public static CallCenter getInstance() {
        if (!IS_INSTANCE_CREATED.get()) {
            INSTANCE_LOCK.lock();
            try {
                if (!IS_INSTANCE_CREATED.get()) {
                    callCenterInstance = new CallCenter();
                    //LOGGER.info("Uber cab company is created!");
                    System.out.println("Call center is created");
                    IS_INSTANCE_CREATED.set(true);
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }
        return callCenterInstance;
    }

    public Operator acquireOperator(Client client) throws Exception {
        try {
            Random rand = new Random();
            semaphore.acquire();
            OPERATOR_LOCK.lock();
            Operator operator = null;
            System.out.printf(" Call centre :%s is phoned the Call center!! \n", client.getName());
            int indexOperator = findRequiredOperator(client);
            if (indexOperator != -1) {
                operator = allOperators.remove(indexOperator);
                System.out.printf("Оператор на связи %s\n", client.getName());
            } else {
                System.out.printf("Дорогой клиент %s: нужный вам оператор %s сейчас занят \n", client.getName(), client.getCallScope());
            }
            return operator;
        } catch (InterruptedException e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            OPERATOR_LOCK.unlock();
        }
    }

    public void releaseOperator(Operator operator) {
        OPERATOR_LOCK.lock();
        try {
            allOperators.add(operator);
            semaphore.release();
            System.out.println((String.format("Колл центр : Оператор  #%d  с вопросом %s свободен! \n",
                    operator.getId(),operator.getCallScope())));
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

