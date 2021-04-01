package pt.uc.dei.student.others;

import pt.uc.dei.student.elections.*;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
//TODO ===================================
//TODO CHECK COMENTARIOS
//TODO ===================================

/**
 * Interface do RMI
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
 * @see Remote
 */
public interface RMI extends Remote {
    /**
     * Manda mensagem para dizer que está a funcionar
     *
     * @return "I'm alive!"
     * @throws java.rmi.RemoteException
     * @throws InterruptedException
     */
    String saySomething() throws java.rmi.RemoteException, InterruptedException;

    /**
     * @param name        nome da pessoa
     * @param cargo
     * @param pass
     * @param dep
     * @param num_phone
     * @param address
     * @param num_cc
     * @param cc_validity
     * @return
     * @throws java.rmi.RemoteException
     * @throws InterruptedException
     */
    boolean insertPerson(String name, String cargo, String pass, int dep, int num_phone, String address, int num_cc, String cc_validity) throws java.rmi.RemoteException, InterruptedException;

    int insertElection(String begin_data, String end_data, String titulo, String descricao, String type_ele) throws java.rmi.RemoteException, InterruptedException;

    boolean insertElectionDepartment(int id_election, int id_dep) throws java.rmi.RemoteException, InterruptedException;

    boolean insertCandidacyIntoElection(String name, String type, int election_id) throws java.rmi.RemoteException, InterruptedException;

    boolean insertPersonIntoCandidacy(int candidacy_id, int cc_number) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Election> getElections() throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Election> getElectionsNotStarted() throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Candidacy> getCandidacies(int election_id) throws java.rmi.RemoteException, InterruptedException;

    Person getPerson(String username, String password) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Person> getPeople(int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    void updateElections(Election e) throws java.rmi.RemoteException, InterruptedException;

    void removeOnDB(String table, String idName, int id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> getDepartments() throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> getActiveMulticasts() throws java.rmi.RemoteException, InterruptedException;

    String initializeMulticast(int dep_id, Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    void initializeRealTimeVotes(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    void initializeRealTimePolls(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    void endRealTimePolls(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> selectPollingStation(int election_id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> selectNoAssociatedPollingStation(int election_id) throws java.rmi.RemoteException, InterruptedException;

    int countRowsBD(String sql, String returnCount) throws java.rmi.RemoteException, InterruptedException;

    int numElections() throws java.rmi.RemoteException, InterruptedException;

    void removePollingStation(int department_id) throws java.rmi.RemoteException, InterruptedException;

    void insertPollingStation(int election_id, int department_id) throws java.rmi.RemoteException, InterruptedException;

    boolean turnOffPollingStation(int department_id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Election> getEndedElections() throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Election> getCurrentElections(int department_id) throws java.rmi.RemoteException, InterruptedException;

    int getBlackVotes(int id_election) throws java.rmi.RemoteException, InterruptedException;

    int getNullVotes(int id_election) throws java.rmi.RemoteException, InterruptedException;

    int getVotesCandidacy(int id_election, int id_candidacy) throws java.rmi.RemoteException, InterruptedException;

    float getPercentVotesCandidacy(int id_election, int id_candidacy) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Person> getRegisPeople(int election_id, int department_id, String campo, String campo_sql, int campo_num) throws java.rmi.RemoteException, InterruptedException;

    void insertVotingRecord(String id_election, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    void updateCandidacyVotes(String id_election, String candidacyOption, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    void updateBlankVotes(String id_election, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    void updateNullVotes(String id_election, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    boolean checkIfAlreadyVote(int cc, int election) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<VotingRecord> getVotingRecords() throws java.rmi.RemoteException, InterruptedException;

    void updateTerminalStatus(String id, String status) throws java.rmi.RemoteException, InterruptedException;

    boolean insertTerminal(String id, int dep_id) throws java.rmi.RemoteException, InterruptedException;

    int getTerminal(String required_id) throws java.rmi.RemoteException, InterruptedException;

    int getElectionIdFromTerminal(String id) throws java.rmi.RemoteException, InterruptedException;

    int getElectorInfo(String id) throws java.rmi.RemoteException, InterruptedException;

    void updateDepartmentMulticast(int id) throws java.rmi.RemoteException, InterruptedException;

    ConcurrentHashMap<Integer, ArrayList<Integer>> getActiveTerminals() throws java.rmi.RemoteException, InterruptedException;

    void endRealTimeInfo(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    ConcurrentHashMap<Integer, Notifier> getNotifiersMulticast() throws java.rmi.RemoteException, InterruptedException;
}

