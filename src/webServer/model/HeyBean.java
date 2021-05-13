package webServer.model;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;
import pt.uc.dei.student.others.RMI;
import pt.uc.dei.student.others.Utilitary;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Date;

public class HeyBean {
    private RMI server;
    private int ccnumber; // username and password supplied by the user
    private String password;
    private int election_id;
    private int candidacy_id;
    // Fields for register
    private String name, cargo, address, ccDate, restriction;
    private int phone, dep;
    // Fields for create an election
    private String title, description, type, iniDate, fimDate;
    private Election e;


    public HeyBean() {
        // Connect to RMI Server
        try {
            server = (RMI) LocateRegistry.getRegistry("127.0.0.1", 7000).lookup("server");
//            server = (RMI) Naming.lookup("server");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getIniDate() {
        return iniDate;
    }

    public String getFimDate() {
        return fimDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setCcDate(String ccDate) {
        this.ccDate = ccDate;
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

    public void setCandidacy_id(int candidacy_id) {
        this.candidacy_id = candidacy_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    public void setFimDate(String fimDate) {
        this.fimDate = fimDate;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
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
            elections = server.getCurrentElectionsPerson(String.valueOf(ccnumber), String.valueOf(password));
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return elections;
    }

    public ArrayList<Election> getAllElections() {
        ArrayList<Election> allElections = null;
        try {
            allElections = server.getElections();
        } catch(RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return allElections;
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

    public ArrayList<Department> getDepartments() {
        ArrayList<Department> departments = null;
        try {
            departments = server.getDepartments();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public void updateVotes() {
        try {
            server.updateCandidacyVotes(String.valueOf(this.election_id), String.valueOf(this.candidacy_id), String.valueOf(this.ccnumber), String.valueOf(0));
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfAlreadyVotes() {
        try {
            return server.checkIfAlreadyVote(this.ccnumber, this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertRegister() {
        try {
            return server.insertPerson(this.name, this.cargo, this.password, this.dep, this.phone, this.address, this.ccnumber, this.ccDate);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertElection() {
        int id_election = -1;
        try {
            id_election = server.insertElection(this.iniDate, this.fimDate, this.title, this.description, this.type);
            if (this.restriction.equals("yes")) {
                server.insertElectionDepartment(id_election, this.dep);
            } else
                server.insertElectionDepartment(id_election, -1);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return id_election;
    }

    // Previne que alguem tente votar (caso não possa) passando logo para a página se escolher as listas e não ter passado pela página de escolher uma eleição
    public boolean checkIfAlreadyVoteOnVoteForm() {
        int election_id;
        try {
            election_id = server.getElectionFromCandidacy(this.candidacy_id);
            // Pra simplificar considera-se que caso uma elição não exista o utilizador já tenha votado nela
            if (election_id == 0 && server.checkIfAlreadyVote(this.ccnumber, election_id))
                return true;
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfSelectedElectionExists() {
        return Utilitary.hasElection(this.election_id, getAllElections());
    }

    public boolean checkSelectedCandidacy_Election() {
        try {
            if (server.checkElectionHasCandidacy(this.election_id, this.candidacy_id) != 0) {
                return true;
            }
        } catch (RemoteException | InterruptedException remoteException) {
            remoteException.printStackTrace();
        }
        return false;
    }

    public int checkIfCanEdit() {
        try {
            return server.checkIfElectionNotStarted(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean editElection() {
        if (checkIfSelectedElectionExists()) {
            try {
                return server.updateElectionOnEdit(this.election_id, this.name, this.type, this.description, this.iniDate, this.fimDate);
            } catch (RemoteException | InterruptedException remoteException) {
                remoteException.printStackTrace();
            }
        }
        return false;
    }
}