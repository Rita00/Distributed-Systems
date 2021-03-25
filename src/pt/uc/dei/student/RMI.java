package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;

import java.rmi.*;
import java.util.ArrayList;

public interface RMI extends Remote {
    String saySomething() throws java.rmi.RemoteException, InterruptedException;

    boolean insertPerson(String cargo, String pass, int dep, int num_phone, String address, int num_cc, String cc_validity) throws java.rmi.RemoteException, InterruptedException;

    int insertElection(String begin_data, String end_data, String titulo, String descricao, String type_ele) throws java.rmi.RemoteException, InterruptedException;

    boolean insertElectionDepartment(int id_election, int id_dep) throws java.rmi.RemoteException, InterruptedException;

    boolean insertCandidacyIntoElection(String name, String type, int election_id) throws java.rmi.RemoteException, InterruptedException;

    boolean insertPersonIntoCandidacy(int candidacy_id, int cc_number) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Election> getElections() throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Candidacy> getCandidacies(int election_id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Person> getPeople(int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    void updateElections(Election e) throws java.rmi.RemoteException, InterruptedException;

    void removeOnDB(String table, String idName, int id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> getDepartments() throws java.rmi.RemoteException, InterruptedException;

    String initializeMulticast(int dep_id, Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> selectPollingStation(int election_id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> selectNoAssociatedPollingStation(int election_id) throws java.rmi.RemoteException, InterruptedException;

    int countRowsBD(String sql) throws java.rmi.RemoteException, InterruptedException;

    int numElections() throws java.rmi.RemoteException, InterruptedException;

    void removePollingStation(int department_id) throws java.rmi.RemoteException, InterruptedException;

    void insertPollingStation(int election_id, int department_id) throws java.rmi.RemoteException, InterruptedException;

    boolean turnOffPollingStation(int department_id) throws java.rmi.RemoteException, InterruptedException;

    boolean checkCorrectPhone(int num_phone) throws java.rmi.RemoteException, InterruptedException;

}

