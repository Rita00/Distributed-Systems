package com.company;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.Thread.sleep;

public class RMIServer extends UnicastRemoteObject implements RMI {
    public RMIServer() throws RemoteException {
        super();
    }

    public String saySomething() throws RemoteException {
        return "I'm alive!";
    }

    static public boolean isPrimaryFunctional() {
        try {
            RMI h = (RMI) LocateRegistry.getRegistry(7000).lookup("test");
            String message = h.saySomething();
            System.out.println(message);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int numPingsFailed = 0;
        while (numPingsFailed < 5) {
            try {
                sleep(200);
                if (!isPrimaryFunctional())
                    numPingsFailed++;
                else numPingsFailed = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Becoming Primary Server!");
        try {
            RMIServer obj = new RMIServer();
            Registry r = LocateRegistry.createRegistry(7000);
            r.rebind("test", obj);
            System.out.println("RMI Server ready!");
        } catch (RemoteException re) {
            System.out.println("Exception in Server.main: " + re);
        }
    }
}
