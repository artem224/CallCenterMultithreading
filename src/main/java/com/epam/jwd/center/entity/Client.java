package com.epam.jwd.center.entity;

import java.util.Objects;

public class Client implements Runnable {


    private int clientId;
    private final String name;
    private final CallCenter callCenter;
    private boolean isOnLine;
    private CallScope callScope;

    public Client(int clientId, String name, CallCenter callCenter, CallScope callScope) {
        this.clientId = clientId;
        this.name = name;
        this.callCenter = callCenter;
        this.callScope = callScope;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public CallScope getCallScope() {
        return callScope;
    }


    public int getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public CallCenter getCallCenter() {
        return callCenter;
    }

    @Override
    public void run() {
        try {
            //LOGGER.info(String.format("%s: is calling to Uber cab company...", name));
            System.out.printf("%s: is calling to call centre with question %s %n \n",name,getCallScope());
            Operator operator = callCenter.acquireOperator(this);

            if (operator == null) {

                //LOGGER.info(String.format("%s: is getting Uber notification of rejecting call " +
                //"and starts finding other taxi companies :(", name));
                System.out.printf("Ждите на линии пожалуйста %s \n", this.getName());
                run();

            } else {

//            LOGGER.info(String.format("%s: is getting Uber notification of accepting call " +
//                    "and starts waiting uber taxi #%d...", name, taxi.getTaxiNumber()));
                operator.answerClient(this);
                Thread.sleep(3000);

                if (isOnLine) {
                    // на линии
//                locationCoordinateX = destinationPointX;
//                locationCoordinateY = destinationPointY;
//                LOGGER.info(String.format("%s: is arrived to destination point!", name));
                    System.out.printf("Разговор с %s окончен \n",this.name);
                } else {
                    System.out.printf("Оставайтесь на линии %s \n" , this.getName());

                    //callCenter.releaseOperator(operator);
                    // не на линии
                    //in this program version it impossible, but in the future versions can be
//                LOGGER.info(String.format("%s: is not arrived to destination point!", name));
                }
                callCenter.releaseOperator(operator);

            }
        } catch (Exception e) {
            //LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", callCenter=" + callCenter +
                ", isOnLine=" + isOnLine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId == client.clientId
                && isOnLine == client.isOnLine
                && name.equals(client.name)
                && callCenter.equals(client.callCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, name, callCenter, isOnLine);
    }
}
