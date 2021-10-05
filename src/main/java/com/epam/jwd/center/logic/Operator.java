package com.epam.jwd.center.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Operator  {

    private static final Logger logger = LogManager.getLogger(Operator.class);
    private final int id;
    private final CallScope callScope;

    public Operator(int id, CallScope callScope) {
        this.id = id;
        this.callScope = callScope;

    }
    public int getId() {
        return id;
    }
    public CallScope getCallScope() {
        return callScope;
    }

    public void answerClient(Client client) {
        logger.info(String.format("OPERATOR : HELLO DEAR CLIENT. " +
                "Уou are connected with operator. Your name is %s\n", client.getName()));
        client.setOnLine(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return id == operator.id && callScope == operator.callScope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, callScope);
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", callScope=" + callScope +
                '}';
    }
}
