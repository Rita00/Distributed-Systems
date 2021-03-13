package com.company;

import java.rmi.*;

public interface RMI extends Remote {
    public String saySomething() throws java.rmi.RemoteException;
}
