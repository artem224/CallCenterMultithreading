package com.epam.jwd.center.entity;

public class Operator  {

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
        System.out.printf("Это оператор. Your name is %s\n", client.getName());
        client.setOnLine(true);
    }
}
