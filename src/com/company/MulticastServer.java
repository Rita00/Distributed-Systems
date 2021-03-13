package com.company;

import java.rmi.registry.LocateRegistry;

public class MulticastServer {
    public static void main(String[] args) {
        try {
            RMI h = (RMI) LocateRegistry.getRegistry(7000).lookup("test");

            String message = h.saySomething();
            System.out.println("HelloClient: " + message);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }
    }
}
