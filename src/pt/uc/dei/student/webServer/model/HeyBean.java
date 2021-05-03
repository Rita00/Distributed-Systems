package pt.uc.dei.student.webServer.model;

import pt.uc.dei.student.others.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class HeyBean {
    private RMI server;
    private int ccnumber; // username and password supplied by the user
    private String password;

    public HeyBean() {
        // Connect to RMI Server
        try {
            server = (RMI) Naming.lookup("server");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }


    public void setUsername(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

/*
import java.util.ArrayList;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import pt.uc.dei.student.others.RMI;

public class HeyBean {
	private RMI server;
	private String username; // username and password supplied by the user
	private String password;

	public HeyBean() {
		try {
			server = (RMI) Naming.lookup("server");
		}
		catch(NotBoundException|MalformedURLException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}

	public ArrayList<String> getAllUsers() throws RemoteException {
		return server.getAllUsers(); // are you going to throw all exceptions?
	}

	public boolean getUserMatchesPassword() throws RemoteException {
		return server.userMatchesPassword(this.username, this.password);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
*/