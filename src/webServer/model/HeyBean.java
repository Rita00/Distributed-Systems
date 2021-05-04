package webServer.model;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;
import pt.uc.dei.student.others.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class HeyBean {
    private RMI server;
    private int ccnumber; // username and password supplied by the user
    private String password;
    private int election_id;

    public HeyBean() {
        // Connect to RMI Server
        try {
            server = (RMI) LocateRegistry.getRegistry("127.0.0.1", 7000).lookup("server");
//            server = (RMI) Naming.lookup("server");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setCcnumber(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setElection_id(int election_id) {
        this.election_id = election_id;
    }

    public Person getUser() throws RemoteException {
        Person p = null;
        try {
            p = server.getPerson(String.valueOf(this.ccnumber), password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return p;
    }

    public ArrayList<Election> getElections() {
        ArrayList<Election> elections = null;

        try {
            elections = server.getCurrentElections(-1);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return elections;
    }

    public ArrayList<Candidacy> getCandidacies() {
        ArrayList<Candidacy> candidacies = null;
        try {
            candidacies = server.getCandidacies(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return candidacies;
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