package pt.uc.dei.student;

import pt.uc.dei.student.elections.*;
import pt.uc.dei.student.others.*;

import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

/**
 * Servidor RMI
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 * @see RMI
 * @see UnicastRemoteObject
 */
public class
RMIServer extends UnicastRemoteObject implements RMI {
    /**
     * Numero maximo de mesas de voto
     */
    private final int NUM_MULTICAST_SERVERS;
    /**
     * Endereço IPv4 do servidor
     */
    private final String SERVER_ADDRESS;
    /**
     * Porte do servidor
     */
    private final int SERVER_PORT;
    /**
     * Porte do RMI
     */
    private final int REGISTRY_PORT;
    /**
     * HashMap que contém callbacks para todas as mesas de voto que foram inicialmente ligadas
     */
    static ConcurrentHashMap<Integer, Notifier> notifiersMulticast;
    /**
     * Array que contém callbacks para todas as consolas de administração que estão a receber o estado dos votos em tempo real
     */
    static ArrayList<Notifier> notifiersVotesAdmin;
    /**
     * Array que contém callbacks para todas as consolas de administração que estão a receber o estado dasa mesas de voto e respetivos terminais de voto
     */
    static ArrayList<Notifier> notifiersPollsAdmin;

    /**
     * Construtor do objeto Servidor RMI
     *
     * @param SERVER_ADDRESS        endereço IPv4 do servidor
     * @param SERVER_PORT           porte do servidor
     * @param REGISTRY_PORT         porte do registo RMI
     * @param NUM_MULTICAST_SERVERS numero maximo de servidores multicast
     * @throws RemoteException falha do RMI
     */
    public RMIServer(String SERVER_ADDRESS, int SERVER_PORT, int REGISTRY_PORT, int NUM_MULTICAST_SERVERS) throws RemoteException {
        super();
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.SERVER_PORT = SERVER_PORT;
        this.REGISTRY_PORT = REGISTRY_PORT;
        this.NUM_MULTICAST_SERVERS = NUM_MULTICAST_SERVERS;
        notifiersMulticast = new ConcurrentHashMap<>();
        notifiersVotesAdmin = new ArrayList<>();
        notifiersPollsAdmin = new ArrayList<>();
    }

    /**
     * Insere uma determinada pessoa na base de dados e
     * encripta a palavra passe
     *
     * @param name        nome
     * @param cargo       cargo (Estudante, Docente ou Funcionário)
     * @param pass        código de acesso para aceder a uma determinada eleição
     * @param dep         departamento a que a pessoa pertence
     * @param num_phone   número de telemóvel
     * @param address     morada
     * @param num_cc      número de cartão de cidadão
     * @param cc_validity formato da data (ano, mes, dia)
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     */
    public boolean insertPerson(String name, String cargo, String pass, int dep, int num_phone, String address, int num_cc, String cc_validity) {
        pass = Utilitary.getPasswordHash(Integer.toString(num_cc), pass);
        String sql = String.format("INSERT INTO person(name,job,password,department_id,phone,address,cc_number,cc_validity) VALUES('%s','%s',?,%s,%s,'%s',%s,'%s')", name, cargo, dep, num_phone, address, num_cc, cc_validity);
        if (sql == null) return false;
        return updateOnDB(sql, pass);
    }

    /**
     * Insere uma determinada eleição na base de dados
     *
     * @param begin_data data de inicio da eleição
     * @param end_data   data de fim da eleição
     * @param titulo     título da eleição
     * @param descricao  descrição da eleição
     * @param type_ele   tipo da eleição (Estudante, Docente ou Funcionário)
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     */
    public int insertElection(String begin_data, String end_data, String titulo, String descricao, String type_ele) {
        String dataIni = String.format("%s:00", begin_data), dataFim = String.format("%s:00", end_data);
        String sql = String.format("INSERT INTO election(title,type,description,begin_date,end_date) VALUES('%s','%s','%s','%s','%s')", titulo, type_ele, descricao, dataIni, dataFim);
        if (sql == null) return -1;
        Connection conn = connectDB();
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            Statement stmt2 = conn.createStatement();
            ResultSet generate_keys = stmt2.executeQuery("SELECT last_insert_rowid()");
            generate_keys.next();
            int auto_id = generate_keys.getInt(1);
            conn.commit();
            stmt.close();
            conn.close();
            return auto_id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    /**
     * Atribui uma eleição a um departamento na entidade election_department da base de dados
     *
     * @param id_election ID da eleição
     * @param id_dep      ID do departamento
     * @return true em caso de sucesso na inserção, false caso contrário
     */
    public boolean insertElectionDepartment(int id_election, int id_dep) {
        String sql = String.format("INSERT INTO election_department(election_id,department_id) VALUES(%s,%s)", id_election, id_dep);
        return updateOnDB(sql);
    }

    /**
     * Insere uma lista na entidade candidacy da base de dados
     *
     * @param name        nome da lista
     * @param type        tipo da lista
     * @param election_id ID da eleição
     * @return true em caso de sucesso na inserção, false caso contrário
     */
    public boolean insertCandidacyIntoElection(String name, String type, int election_id) {
        return this.updateOnDB(String.format("INSERT INTO candidacy(id,name,type,election_id) VALUES (NULL,'%s','%s',%s);", name, type, election_id));
    }

    /**
     * Atribui uma pessoa a uma lista na entidade candidacy_person da base de dados,
     * e verifica se a pessoa pode candidatar-se.
     *
     * @param election_id   ID da eleição da lista
     * @param candidacy_id  ID da lista
     * @param cc_number     número de cartão de cidadão da pessoa
     * @return String com a pensagem de erro
     */
    public String insertPersonIntoCandidacy(int election_id, int candidacy_id, int cc_number) {
        Person p = this.selectPeople("SELECT * FROM person WHERE cc_number="+cc_number).get(0);
        Candidacy c = this.selectCandidacies("SELECT * FROM candidacy WHERE id="+candidacy_id).get(0);
        int dep_id = this.countRowsBD("election_department WHERE election_id="+election_id,"department_id");
        if(p!=null){
            if(p.getJob().equals(c.getType()) && (dep_id==-1 || p.getDepartment_id()==dep_id)){
                if(this.updateOnDB(String.format("INSERT INTO candidacy_person(candidacy_id,person_cc_number) VALUES (%s,%s);", candidacy_id, cc_number))){
                    return "";
                }else{
                    return "A pessoa já está associada a uma lista";
                }
            }else{
                return "O candidator não pode ser adicionado a esta lista";
            }
        }else{
            return "Não forma encontradas pessoas com esse numero de carão de cidadão";
        }
    }

    /**
     * Procura todas as eleições na base de dados
     *
     * @return todas as eleições
     */
    public ArrayList<Election> getElections() {
        return this.selectElections("SELECT * FROM election");
    }

    /**
     * Procura todas as eleições que não começaram na base de dados
     *
     * @return todas as eleições que não começaram
     */
    public ArrayList<Election> getElectionsNotStarted() {
        return this.selectElections("SELECT * FROM election where begin_date > date('now') AND end_date > date('now')");
    }

    /**
     * Procura todas as listas de uma determinada eleição na base de dados
     *
     * @param election_id ID da eleição
     * @return todas as listas da eleição
     */
    public ArrayList<Candidacy> getCandidacies(int election_id) {
        return this.selectCandidacies("SELECT * FROM candidacy WHERE election_id = " + election_id);
    }

    /**
     * Procura uma pessoa pelas suas cardenciais na base de dados
     *
     * @param username número de cartão de cidadão
     * @param password código de acesso
     * @return pessoa pesquisada ou null caso não seja encontrada
     */
    public Person getPerson(String username, String password) {
        try {
            password = Utilitary.getPasswordHash(username, password);
            ArrayList<Person> people = this.selectPeople("SELECT * FROM person WHERE cc_number='" + username + "' AND password=?;", password);
            if (people.size() != 0)
                return people.get(0);
            else return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Pesquisa todas as pessoas atribuidas a uma determinada lista
     *
     * @param candidacy_id ID da lista
     * @return todas as pessoas atribuidas à lista
     */
    public ArrayList<Person> getPeople(int candidacy_id) {
        return this.selectPeople(
                "SELECT *\n" +
                        "FROM person\n" +
                        "WHERE cc_number IN (\n" +
                        "    SELECT person_cc_number\n" +
                        "    FROM candidacy_person\n" +
                        "    WHERE candidacy_id = " + candidacy_id + "\n" +
                        "); "
        );
    }

    /**
     * Atualiza dados de uma eleição,
     * devolve mensagem de sucesso caso a atualização seja bem sucedida
     * ou de erro caso contrário
     *
     * @param e eleição para atualizar
     */
    public void updateElections(Election e) {
        if (this.updateOnDB(String.format("UPDATE election SET title='%s',type='%s',description='%s',begin_date='%s',end_date='%s' WHERE id=%s", e.getTitle(), e.getType(), e.getDescription(), e.getBegin().toString(), e.getEnd().toString(), e.getId()))) {
            System.out.println("Successfully updated election");
        } else {
            System.out.println("Problem updating election");
        }
    }

    /**
     * Insere dados sobre um terminal de voto na base de dados
     *
     * @param id     ID do terminal de voto
     * @param dep_id ID do departamento
     */
    public void insertTerminal(String id, int dep_id) {
        updateOnDB(String.format("INSERT INTO voting_terminal (id, department_id, status, infoPerson) VALUES(%s,%s,1,0)", id, dep_id));
        this.sendRealTimePolls();
    }

    /**
     * Atualiza estado de um terminal de voto na base de dados
     *
     * @param id     ID do terminal de voto
     * @param status estado do terminal de voto
     */
    public void updateTerminalStatus(String id, String status) {
        updateOnDB("UPDATE voting_terminal SET status = " + status + " WHERE id = '" + id + "'");
        this.sendRealTimePolls();
    }

    /**
     * Atualiza a informação sobre a pessoa no terminal de voto
     *
     * @param cc_number  número de cartão de cidadão
     * @param idTerminal ID do terminal de voto
     */
    public void updateTerminalInfoPerson(int cc_number, String idTerminal) {
        updateOnDB("UPDATE voting_terminal SET infoPerson = " + cc_number + " WHERE id = " + idTerminal);
    }

    /**
     * Atualiza a informação sobre a eleição atribuida ao terminal de voto para votar
     *
     * @param election_id ID da eleição
     * @param idTerminal  ID do terminal de voto
     */
    public void updateTerminalInfoElection(int election_id, String idTerminal) {
        updateOnDB("UPDATE voting_terminal SET infoElection = " + election_id + " WHERE id = " + idTerminal);
    }

    /**
     * Pesquisa o se o ID requerido pelo terminal de voto está a ser utilizado
     *
     * @param required_id ID requirido pelo terminal de voto
     * @return -1 caso o ID esteja disponivel ou o estado do terminal caso esteja indisponivel
     */
    public int getTerminal(String required_id) {
        if (countRowsBD("voting_terminal WHERE id = " + required_id, null) == 0) {
            return -1;
        } else {
            return countRowsBD("voting_terminal WHERE id = " + required_id, "status");
        }
    }

    /**
     * Procura o ID da eleição a ser votada num determinado terminal de voto
     *
     * @param id ID do terminal de voto
     * @return ID da eleição
     */
    public int getElectionIdFromTerminal(String id) {
        return countRowsBD("voting_terminal WHERE id = " + id, "infoElection");
    }

    /**
     * Procura o numero de cartão de cidadão da pessoa que está
     * a votar num determinado terminal de voto
     *
     * @param id ID do terminal de voto
     * @return numero de cartão de cidadão da pessoa
     */
    public int getElectorInfo(String id) {
        return countRowsBD("voting_terminal WHERE id = " + id, "infoPerson");
    }

    /**
     * Remove na base de dados,
     * devolve mensagem de sucesso caso a remoção seja bem sucedida
     * ou de erro caso contrário
     *
     * @param table  entidade onde é feita a remoção
     * @param idName nome da chave primária
     * @param id     valor da chave a remover
     */
    public void removeOnDB(String table, String idName, int id) {
        if (this.updateOnDB("DELETE FROM " + table + " WHERE " + idName + " = " + id)) {
            System.out.println("Removed from" + table + " id #" + id);
        } else {
            System.out.println("Problem removing id #" + id + " from database");
        }
    }

    /**
     * Procura todos os departamentos na base de dados
     *
     * @return departamentos da base de dados
     */
    public ArrayList<Department> getDepartments() {
        return this.selectDepartments("SELECT * FROM department");
    }

    /**
     * Efetua a ligação à base de dados
     *
     * @return Objeto Connection relativo à base de dados
     */
    public Connection connectDB() {
        String url = "jdbc:sqlite:Election.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    /**
     * Insere/Remove na base de dados na respetivas tabela
     *
     * @param sql commando sql
     * @return true ou false dependendo se a inserção teve ou não sucesso
     */
    public boolean updateOnDB(String sql) {
        Connection conn = connectDB();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Insere/Remove na base de dados na respetivas tabela
     *
     * @param sql           commando sql
     * @param argument1     argumento designado por "?" no comando sql
     * @return true ou false dependendo se a inserção teve ou não sucesso
     */
    public boolean updateOnDB(String sql, String argument1) {
        Connection conn = connectDB();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, argument1);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Seleciona eleições na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Election> selectElections(String sql) {
        Connection conn = connectDB();
        ArrayList<Election> elections = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                elections.add(new Election(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getString("begin_date"),
                        rs.getString("end_date")
                ));
            }
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return elections;
    }

    /**
     * Seleciona listas(candidaturas) na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Candidacy> selectCandidacies(String sql) {
        Connection conn = connectDB();
        ArrayList<Candidacy> candidacies = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                candidacies.add(new Candidacy(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type")
                ));
            }
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return candidacies;
    }

    /**
     * Seleciona listas(candidaturas) na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Person> selectPeople(String sql) {
        Connection conn = connectDB();
        ArrayList<Person> people = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                people.add(new Person(
                        rs.getString("name"),
                        rs.getInt("cc_number"),
                        rs.getString("cc_validity"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getInt("phone"),
                        rs.getString("job"),
                        rs.getInt("department_id")
                ));
            }
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return people;
    }

    /**
     * Seleciona pessoas na base de dados
     *
     * @param sql       comando SQL
     * @param password  palavra passe
     * @return arrayList com as pessoas
     */
    public ArrayList<Person> selectPeople(String sql, String password) {
        Connection conn = connectDB();
        ArrayList<Person> people = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, password);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                people.add(new Person(
                        rs.getString("name"),
                        rs.getInt("cc_number"),
                        rs.getString("cc_validity"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getInt("phone"),
                        rs.getString("job"),
                        rs.getInt("department_id")
                ));
            }
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return people;
    }

    /**
     * Seleciona departamentos na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Department> selectDepartments(String sql) {
        Connection conn = connectDB();
        ArrayList<Department> departments = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return departments;
    }

    /**
     * Seleciona departamentos na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<VotingRecord> selectVotingRecords(String sql) {
        Connection conn = connectDB();
        ArrayList<VotingRecord> votingRecords = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                votingRecords.add(new VotingRecord(
                        formatter.parse(rs.getString("date")),
                        rs.getString("d_name"),
                        rs.getString("p_name"),
                        rs.getString("title")
                ));
            }
            stmt.close();
            conn.close();
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return votingRecords;
    }

    /**
     * Seleciona os terminais de voto na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ConcurrentHashMap<Integer, ArrayList<Integer>> selectActiveTerminals(String sql) {
        Connection conn = connectDB();
        ConcurrentHashMap<Integer, ArrayList<Integer>> depToTerm = new ConcurrentHashMap<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Integer> terminals;
            while (rs.next()) {
                int key = rs.getInt("department_id");
                int value = rs.getInt("id");
                if (depToTerm.containsKey(key)) {
                    terminals = depToTerm.get(key);
                    terminals.add(value);
                } else {
                    terminals = new ArrayList<>();
                    terminals.add(value);
                    depToTerm.put(key, terminals);
                }
            }
            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return depToTerm;
    }

    /**
     * seleciona os departamentos associados a uma determinada eleição
     *
     * @param election_id ID da eleição
     * @return departamentos
     */
    public ArrayList<Department> selectPollingStation(int election_id) {
        /*if (election_id == -1) {
            return selectDepartments("SELECT id, name FROM department WHERE hasmulticastserver = 1");
        } else {*/
        return selectDepartments("SELECT id, name FROM department, election_department " +
                "WHERE department.id = election_department.department_id AND election_department.election_id = " + election_id);

    }


    /**
     * Seleciona as mesas de voto que não estejam associadas a uma determinada eleição
     *
     * @param election_id ID da eleição
     * @return departamentos
     */
    public ArrayList<Department> selectNoAssociatedPollingStation(int election_id) {
        return selectDepartments("SELECT id, name FROM department WHERE department.hasmulticastserver = 1 " +
                "EXCEPT " +
                "SELECT id, name FROM department, election_department " +
                "WHERE department.id = election_department.department_id AND election_department.election_id = " + election_id);
    }

    /**
     * Conta as linhas na base de dados consoante o comando e o atributo
     *
     * @param sql         comando sql
     * @param returnCount atributo para contar
     * @return quantidade contada
     */
    public int countRowsBD(String sql, String returnCount) {
        Connection conn = connectDB();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res;
            if (returnCount == null)
                res = stmt.executeQuery("SELECT COUNT(*) FROM " + sql);
            else res = stmt.executeQuery("SELECT " + returnCount + " FROM " + sql);
            int count = res.getInt(1);
            stmt.close();
            conn.close();
            return count;
        } catch (Exception e) {
            System.out.println("Erro a contar o número de linhas da tabela");
            //e.printStackTrace();
        }
        return 0;
    }

    /**
     * Remove um departamento de uma eleição na base de dados
     *
     * @param department_id ID do departamento
     */
    public void removePollingStation(int department_id) {
        removeOnDB("election_department", "department_id", department_id);
    }

    /**
     * Associa uma eleição a um departamento na base de dados
     *
     * @param election_id   ID da eleição
     * @param department_id ID do departamento
     */
    public void insertPollingStation(int election_id, int department_id) {
        insertElectionDepartment(election_id, department_id);
    }

    /**
     * Altera o para "desligado" o estado de uma mesa de voto
     * para um determinado departamento na base de dados
     *
     * @param department_id ID do departamento
     */
    public void turnOffPollingStation(int department_id) {
        updateOnDB("UPDATE department SET hasMulticastServer = 0 WHERE id = " + department_id);
        sendRealTimePolls();
    }

    /**
     * Procura as eleições passadas na base de dados
     *
     * @return eleições passadas
     */
    public ArrayList<Election> getEndedElections() {
        return selectElections("SELECT * FROM election WHERE end_date < date('now')");
    }

    /**
     * Procura as eleições a decorrerem para um
     * determinado departamento na base de dados
     *
     * @param department_id ID do departamento
     * @return eleições a decorrerem no departamento
     */
    public ArrayList<Election> getCurrentElections(int department_id) {
        return selectElections("SELECT * FROM election, election_department WHERE begin_date <= date('now') AND end_date >= date('now') AND election.id = election_department.election_id AND (department_id = " + department_id + " OR department_id = -1)");
    }

    /**
     * Devolve o numero de votos em branco para uma
     * determinada eleição na base de dados
     *
     * @param id_election ID da eleição
     * @return numero de votos em branco da eleição
     */
    public int getBlackVotes(int id_election) {
        return countRowsBD("election WHERE id = " + id_election, "blank_votes");
    }

    /**
     * Devolve o numero de votos nulos para uma
     * determinada eleição na base de dados
     *
     * @param id_election ID da eleição
     * @return numero de votos nulos da eleição
     */
    public int getNullVotes(int id_election) {
        return countRowsBD("election WHERE id = " + id_election, "null_votes");
    }

    /**
     * Devolve o numero de votos para uma lista numa eleição na base de dados
     *
     * @param id_election  ID da eleição
     * @param id_candidacy ID da lista
     * @return numero de votos
     */
    public int getVotesCandidacy(int id_election, int id_candidacy) {
        return countRowsBD("candidacy WHERE id = " + id_candidacy + " and election_id = " + id_election, "votes");
    }

    /**
     * Calcula a percentagem de votos para uma lista ou
     * calcula a percentagem de votos em branco caso o ID da lista é -1
     *
     * @param id_election  ID da eleição
     * @param id_candidacy ID da lista
     * @return percentagem de votos
     */
    public float getPercentVotesCandidacy(int id_election, int id_candidacy) {
        int totalVotes = countRowsBD("candidacy", "SUM(votes)") + getBlackVotes(id_election) + getNullVotes(id_election);
        if (id_candidacy == -1) return ((float) getBlackVotes(id_election) / (float) totalVotes) * 100;
        else return ((float) getVotesCandidacy(id_election, id_candidacy) / (float) totalVotes) * 100;
    }

    /**
     * Procura pessoas na base de dados cujo os dados correpondam
     *
     * @param election_id   ID da eleição
     * @param department_id ID do departamento
     * @param campo         nome do que está a ser pesquisado (i.e. nome da pessoa, nome da rua,...)
     * @param campo_sql     nome do atributo da pessoa a ser pesquisado (i.e. nome, rua, ...)
     * @param campo_num     nome do que está a ser pesquisado (i.e. numero de cc, numero de telemovel, ...)
     * @return lista de pessoas
     */
    public ArrayList<Person> getRegisPeople(int election_id, int department_id, String campo, String campo_sql, int campo_num) {
        // Verificar se a eleição está restringida a um departamento
        int ndep = countRowsBD("election_department WHERE election_id = " + election_id, "department_id");
        if (ndep == -1) {
            if (campo_num == -1) {
                if (campo_sql.equals("name") || campo_sql.equals("address")) {
                    return selectPeople("SELECT DISTINCT * FROM person " +
                            "JOIN election_department ed " +
                            "JOIN election e on ed.election_id = e.id AND e.type = person.job AND ed.election_id = " + election_id +
                            " AND person." + campo_sql + " like '%" + campo + "%' AND ed.department_id = -1");

                }
                return selectPeople("SELECT DISTINCT * " +
                        "FROM person JOIN election_department ed JOIN election e on ed.election_id = e.id AND e.type = person.job" +
                        " AND ed.election_id = " + election_id + " AND person." + campo_sql + " = '" + campo + "' AND ed.department_id = -1");
            } else {
                return selectPeople("SELECT DISTINCT * " +
                        "FROM person JOIN election_department ed JOIN election e on ed.election_id = e.id AND e.type = person.job" +
                        " AND ed.election_id = " + election_id + " AND person." + campo_sql + " = " + campo_num + " AND ed.department_id = -1");
            }
        } else { // Caso esteja restringido a um determinado departamento
            if (campo_num == -1) {
                if (campo_sql.equals("name") || campo_sql.equals("address")) {
                    return selectPeople("SELECT DISTINCT * " +
                            "FROM person JOIN election_department ed on person.department_id = ed.department_id JOIN election e on ed.election_id = e.id AND e.type = person.job" +
                            " AND ed.department_id = " + department_id + " AND ed.election_id = " + election_id + " AND person." + campo_sql + " like '%" + campo + "%'");
                }
                return selectPeople("SELECT DISTINCT * " +
                        "FROM person JOIN election_department ed on person.department_id = ed.department_id JOIN election e on ed.election_id = e.id AND e.type = person.job" +
                        " AND ed.department_id = " + department_id + " AND ed.election_id = " + election_id + " AND person." + campo_sql + " = '" + campo + "'");
            } else {
                return selectPeople("SELECT DISTINCT * " +
                        "FROM person JOIN election_department ed on person.department_id = ed.department_id JOIN election e on ed.election_id = e.id AND e.type = person.job" +
                        " AND ed.department_id = " + department_id + " AND ed.election_id = " + election_id + " AND person." + campo_sql + " = " + campo_num + "");
            }
        }
    }

    /**
     * Inserir um registo de voto na base de dados
     *
     * @param id_election ID da eleição
     * @param cc          numero de cartão de cidadão
     * @param ndep        ID do departamento
     */
    public void insertVotingRecord(String id_election, String cc, String ndep) {
        updateOnDB(String.format("INSERT INTO voting_record(vote_date,department,person_cc_number,election_id) VALUES(datetime('now'),'%s','%s','%s')", ndep, cc, id_election));
    }

    /**
     * Atualiza o numero de votos de uma lista na base de dados
     * e regista o voto do eleitor
     *
     * @param id_election     ID da eleição
     * @param candidacyOption ID da lista
     * @param cc              numero de cartão de cidadão
     * @param ndep            ID do departamento
     */
    public void updateCandidacyVotes(String id_election, String candidacyOption, String cc, String ndep) {
        insertVotingRecord(id_election, cc, ndep);
        updateOnDB("UPDATE candidacy SET votes = votes + 1 WHERE election_id = " + id_election + " AND id = " + candidacyOption);
        this.sendRealTimeVotes();
    }

    /**
     * Atualiza o numero de votos em branco de uma eleição na base de dados
     * e regista o voto do eleitor
     *
     * @param id_election ID da eleição
     * @param cc          numero de cartão de cidadão
     * @param ndep        ID do departamento
     */
    public void updateBlankVotes(String id_election, String cc, String ndep) {
        insertVotingRecord(id_election, cc, ndep);
        updateOnDB("UPDATE election SET blank_votes = blank_votes + 1 WHERE id = " + id_election);
        this.sendRealTimeVotes();
    }

    /**
     * Atualiza o numero de votos nulos de uma eleição na base de dados
     * e regista o voto do eleitor
     *
     * @param id_election ID da eleição
     * @param cc          numero de cartão de cidadão
     * @param ndep        ID do departamento
     */
    public void updateNullVotes(String id_election, String cc, String ndep) {
        insertVotingRecord(id_election, cc, ndep);
        updateOnDB("UPDATE election SET null_votes = null_votes + 1 WHERE id = " + id_election);
        this.sendRealTimeVotes();
    }

    /**
     * Envia informação sobre os votos via callback para todos os admins que estão a receber informação em tempo real
     */
    public void sendRealTimeVotes() {
        String sql = "SELECT count(*), d.name as Name, e.title as Title" +
                " FROM voting_record" +
                " JOIN department d on voting_record.department = d.id" +
                " JOIN election e on e.id = voting_record.election_id" +
                " WHERE e.begin_date < date('now') AND e.end_date > date('now') GROUP BY voting_record.department, voting_record.election_id";
        ArrayList<InfoElectors> info = new ArrayList<>();
        Connection conn = connectDB();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                info.add(new InfoElectors(
                        rs.getInt("count(*)"),
                        rs.getString("Name"),
                        rs.getString("Title")
                ));
            }
            stmt.close();
            conn.close();
            for (Notifier notifier : notifiersVotesAdmin) {
                try {
                    notifier.updateAdmin(info);
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Envia informação sobre os votos via callback para o admin
     *
     * @param NOTIFIER notifier
     */
    public void sendRealTimeVotes(Notifier NOTIFIER) {
        String sql = "SELECT count(*), d.name as Name, e.title as Title" +
                " FROM voting_record" +
                " JOIN department d on voting_record.department = d.id" +
                " JOIN election e on e.id = voting_record.election_id" +
                " WHERE e.begin_date < date('now') AND e.end_date > date('now') group by voting_record.department, voting_record.election_id";
        ArrayList<InfoElectors> info = new ArrayList<>();
        Connection conn = connectDB();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                info.add(new InfoElectors(
                        rs.getInt("count(*)"),
                        rs.getString("Name"),
                        rs.getString("Title")
                ));
            }
            stmt.close();
            conn.close();
            try {
                NOTIFIER.updateAdmin(info);
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Envia informação sobre as mesas de votos e respetivos terminais de voto via callback
     * para todos os admins que estão a receber informação em tempo real
     */
    public void sendRealTimePolls() {
        String sql = "SELECT department.name as depname, department.hasmulticastserver as statusPoll, vt.id as terminalId, status as statusTerminal FROM department " +
                "LEFT JOIN voting_terminal vt on department.id = vt.department_id WHERE hasmulticastserver not null";
        ArrayList<InfoPolls> info = new ArrayList<>();
        Connection conn = connectDB();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                info.add(new InfoPolls(
                        rs.getString("depname"),
                        rs.getInt("statusPoll"),
                        rs.getInt("terminalId"),
                        rs.getInt("statusTerminal")
                ));
            }
            stmt.close();
            conn.close();
            for (Notifier notifier : notifiersPollsAdmin) {
                try {
                    notifier.updatePollsAdmin(info);
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Envia informação sobre as mesas de votos e respetivos terminais de voto via callback para o admin
     *
     * @param NOTIFIER notifier
     */
    public void sendRealTimePolls(Notifier NOTIFIER) {
        String sql = "SELECT department.name as depname, department.hasmulticastserver as statusPoll, vt.id as terminalId, status as statusTerminal FROM department " +
                "LEFT JOIN voting_terminal vt on department.id = vt.department_id WHERE hasmulticastserver not null";
        ArrayList<InfoPolls> info = new ArrayList<>();
        Connection conn = connectDB();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                info.add(new InfoPolls(
                        rs.getString("depname"),
                        rs.getInt("statusPoll"),
                        rs.getInt("terminalId"),
                        rs.getInt("statusTerminal")
                ));
            }
            stmt.close();
            conn.close();
            try {
                NOTIFIER.updatePollsAdmin(info);
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Verifica se uma determinada pessoa já votou para uma eleição
     *
     * @param cc       numero de cartão de cidadão
     * @param election ID da eleição
     * @return true se já votou, false caso contrário
     */
    public boolean checkIfAlreadyVote(int cc, int election) {
        return countRowsBD("voting_record WHERE person_cc_number = " + cc + " AND election_id = " + election, null) > 0;
    }

    /**
     * Procura todos os registos de voto na base de dados
     *
     * @return registos de voto
     */
    public ArrayList<VotingRecord> getVotingRecords() {
        return selectVotingRecords("select vote_date as date, d.name as d_name, p.name as p_name, title FROM voting_record " +
                "JOIN person p on voting_record.person_cc_number = p.cc_number " +
                "JOIN election e on voting_record.election_id = e.id " +
                "JOIN department d on d.id = voting_record.department " +
                "ORDER BY voting_record.election_id, p.name");
    }

    /**
     * Manda mensagem para dizer que está a funcionar
     *
     * @return "I'm alive!"
     */
    public String saySomething() {
        return "I'm alive!";
    }

    /**
     * O servidor secundário envia pings para o servidor primário
     * para cetificar-se que esteja a funcionar
     *
     * @return true ou false caso o servidor secundário consiga enviar com sucesso ou não pings ao servidor primário
     */
    public boolean isPrimaryFunctional() {
        DatagramSocket aSocket = null;
        boolean ok = true;
        try {
            aSocket = new DatagramSocket();
            //System.out.print("Mensagem a enviar = ");

            InetAddress aHost = InetAddress.getByName(this.SERVER_ADDRESS);
            byte[] m = "Pinging".getBytes();
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, this.SERVER_PORT);
            aSocket.send(request);
            aSocket.setSoTimeout(200);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            //System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
        } catch (SocketException e) {
//            System.out.println("Socket: " + e.getMessage());
            ok = false;
        } catch (IOException e) {
//            System.out.println("IO: " + e.getMessage());
            ok = false;
        } finally {
            if (aSocket != null) aSocket.close();
        }
        return ok;
    }

    /**
     * Verifica se uma determinada mesa de voto se pode ligar <br>
     * Pode-se ligar, caso o máximo de mesas de voto ativas ainda não tenha sido atingido
     * e o departamento escolhido ainda não tenha uma mesa de voto ativa
     *
     * @param dep_id   ID do departamento
     * @param NOTIFIER notifier
     * @return nome da mesa de voto criada ou null
     */
    public String initializeMulticast(int dep_id, Notifier NOTIFIER) {
        int numMulticast = countRowsBD("department WHERE hasMulticastServer = 1", null);

        if (numMulticast < NUM_MULTICAST_SERVERS && countRowsBD("department WHERE (hasMulticastServer IS NULL OR hasMulticastServer = 0) AND id = " + dep_id, null) != 0) {
            if (!updateOnDB("UPDATE department SET hasMulticastServer = 1 WHERE id = " + dep_id)) {
                System.out.println("Impossível adicionar mesa de voto! :(");
                return null;
            } else {
                sendRealTimePolls();
                System.out.println("Mesa de voto criada com sucesso! :)");
                synchronized (notifiersMulticast) {
                    notifiersMulticast.put(dep_id, NOTIFIER);
                }
                try {
                    notifiersMulticast.get(dep_id).ping();
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                }
                return selectDepartments("SELECT * FROM department WHERE id = " + dep_id).get(0).getName();
            }
        }
        return null;
    }

    /**
     * Adiciona callback de admin à lista de callbacks para envio de informação de tempo real sobre os votos
     * e envia de imediato a informação mais recente disponível
     *
     * @param NOTIFIER notifier
     */
    public void initializeRealTimeVotes(Notifier NOTIFIER) {
        notifiersVotesAdmin.add(NOTIFIER);
        sendRealTimeVotes(NOTIFIER);
    }

    /**
     * Remove callback de admin da lista de callbacks para envio de informação de tempo real sobre os votos
     *
     * @param NOTIFIER notifier
     */
    public void endRealTimeInfo(Notifier NOTIFIER) {
        notifiersVotesAdmin.remove(NOTIFIER);
    }

    /**
     * Adiciona callback de admin à lista de callbacks para envio de informação de tempo real sobre as mesas de voto
     * e envia de imediato a informação mais recente disponível
     *
     * @param NOTIFIER notifier
     */
    public void initializeRealTimePolls(Notifier NOTIFIER) {
        notifiersPollsAdmin.add(NOTIFIER);
        sendRealTimePolls(NOTIFIER);
    }

    /**
     * Remove callback de admin da lista de callbacks para envio de informação de tempo real sobre as mesas de voto
     *
     * @param NOTIFIER notifier
     */
    public void endRealTimePolls(Notifier NOTIFIER) {
        notifiersPollsAdmin.remove(NOTIFIER);
    }

    /**
     * Inicializa a ligação do Servidor RMI com o Servidor Multicast
     */
    public void initializeRMI() {
        System.out.println("Becoming Primary Server!");
        try {
            RMIServer obj = new RMIServer(this.SERVER_ADDRESS, this.SERVER_PORT, this.REGISTRY_PORT, this.NUM_MULTICAST_SERVERS);
            Registry r = LocateRegistry.createRegistry(this.REGISTRY_PORT);
            r.rebind("clientMulticast", obj);
            r.rebind("admin", obj);
            System.out.println("RMI Server ready!");
        } catch (RemoteException re) {
            System.out.println("Exception in Server.main: " + re);
        }
    }

    /**
     * Inicializa a ligação do servidor primário com o servidor secundário via UDP <br>
     * Necessário para verificar se o servidor primária se mantém ligado, caso contrário o servidor secundário passa a ser primário <br>
     * Quando o servidor primário que deixou de estar funcional se reconectar irá assumir o papel de servidor secundário
     */
    public void initializeUDP() {
        String s;
        try (DatagramSocket aSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("Socket Datagram à escuta no porto 7001");
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                s = new String(request.getData(), 0, request.getLength());
//                System.out.println("Recebeu um ping: " + s);
                buffer = "I'm Alive".getBytes();
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
//            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
//            System.out.println("IO: " + e.getMessage());
        }
    }

    /**
     * Inicializa o verfificador de estado
     */
    private void initializeStatusChecker() {
        new StatusChecker(notifiersMulticast, this);
    }

    /**
     * Leitura dos dados no property file,
     * envia 5 pings para decidir se vai ser servidor primário,
     * inicializa o servidor RMI e as ligações UDP
     *
     * @param args argumentos de entrada do programa
     * @throws IOException problema na leitura do property file
     */
    public static void main(String[] args) throws IOException {
        /*
         * PROPERTIES
         */
        FileReader reader;
        try {
            //PARA OS JAR
            reader = new FileReader("config.properties");
        } catch (IOException e) {
            //PARA CORRER NO IDE
            reader = new FileReader("src/pt/uc/dei/student/config.properties");
        }
        Properties p = new Properties();
        p.load(reader);
        int REGISTRY_PORT = Integer.parseInt(p.getProperty("rmiRegistryPort"));
        System.out.println("REGISTRY_PORT: " + REGISTRY_PORT);
        int SERVER_PORT = Integer.parseInt(p.getProperty("rmiServerPort"));
        System.out.println("SERVER_PORT: " + SERVER_PORT);
        String SERVER_ADDRESS = p.getProperty("rmiServerAddress");
        System.out.println("SERVER_ADDRESS: " + SERVER_ADDRESS);
        System.setProperty("java.rmi.server.hostname", SERVER_ADDRESS);
        int NUM_MULTICAST_SERVERS = Integer.parseInt(p.getProperty("numMulticastServers"));
        System.out.println("NUM_MULTICAST_SERVERS: " + NUM_MULTICAST_SERVERS);
        /*
         * RMI
         */
        RMIServer rmiServer = new RMIServer(SERVER_ADDRESS, SERVER_PORT, REGISTRY_PORT, NUM_MULTICAST_SERVERS);
        int numPingsFailed = 0;
        while (numPingsFailed < 5) {
            try {
                sleep(200);
                if (!rmiServer.isPrimaryFunctional())
                    numPingsFailed++;
                else numPingsFailed = 0;
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
        rmiServer.initializeRMI();
        rmiServer.initializeStatusChecker();
        rmiServer.initializeUDP();
    }
}
