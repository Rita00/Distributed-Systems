package com.company;

import java.rmi.registry.LocateRegistry;

public class AdminConsole {
    public static void main(String[] args) {
        try {
            RMI h = (RMI) LocateRegistry.getRegistry(7000).lookup("admin");
            String message = h.saySomething();
            System.out.println("Hello Admin: " + message);
        } catch (Exception e) {
            System.out.println("Exception in Admin: " + e);
            e.printStackTrace();
        }
    }
}
