package com.epam.jwd.center.controller;

import com.epam.jwd.center.entity.CallCenter;
import com.epam.jwd.center.entity.CallScope;
import com.epam.jwd.center.entity.Client;
import com.epam.jwd.center.entity.Operator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] args) {
        CallCenter callCenter = CallCenter.getInstance();
        Client client5 = new Client(5,"Кирилл", callCenter,CallScope.TECH);
        Client client1 = new Client(1,"Артём", callCenter, CallScope.TECH);
        Client client2 = new Client(2,"дима", callCenter,CallScope.TECH);
        Client client3 = new Client(3,"Саня", callCenter,CallScope.TECH);
        Client client4 = new Client(4,"Петя", callCenter,CallScope.TECH);

        List<Client> clients = Arrays.asList(client1,client2,client3,client4,client5);
        ExecutorService service = Executors.newFixedThreadPool(clients.size());
        clients.forEach(service::execute);
        service.shutdown();

//        ArrayBlockingQueue<Operator> queue = new ArrayBlockingQueue<Operator>(20);
//        queue.add();

    }
}
