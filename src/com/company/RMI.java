package com.company;

import java.rmi.*;

public interface RMI extends Remote {
    String saySomething() throws java.rmi.RemoteException, InterruptedException;
}
