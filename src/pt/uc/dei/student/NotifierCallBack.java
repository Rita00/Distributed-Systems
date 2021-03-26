package pt.uc.dei.student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NotifierCallBack extends UnicastRemoteObject implements Notifier {
    public NotifierCallBack() throws RemoteException {
        super();
    }

    public boolean ping()  throws RemoteException{
        return true;
    }
}
