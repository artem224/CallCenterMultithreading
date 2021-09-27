package com.epam.jwd.center.entity;

import com.epam.jwd.center.exceptions.CallCenterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Client implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);
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
            logger.info(String.format("CLIENT: %s: is calling to call centre with question %s %n \n",this.name,this.getCallScope()));
            Operator operator = callCenter.acquireOperator(this);
            if (operator == null) {
                logger.info(String.format("CLIENT: %s: wait on the line please \n",this.name));
                Thread.sleep(2000);
                run();
            } else {
                operator.answerClient(this);
                Thread.sleep(4000);
                if (isOnLine) {
                    logger.info(String.format("CLIENT : Conversation with %s finished \n",this.name));
                } else {
                    logger.info(String.format("CLIENT : Stay on line %s \n" , this.getName()));
                }
                callCenter.releaseOperator(operator);

            }
        } catch (CallCenterException | InterruptedException e) {
            logger.error(e.getMessage(),e);
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
