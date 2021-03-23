package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;

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
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class RMIServer extends UnicastRemoteObject implements RMI {
    public RMIServer() throws RemoteException {
        super();
    }


    /**
     * Função de inserção de uma determinada pessoa na base de dados
     *
     * @param cargo     Cargo (Estudante, Docente ou Funcionário)
     * @param pass      código de acesso para aceder a uma determinada eleição
     * @param dep       Departamento a que a pessoa pertence
     * @param num_phone Número de telemóvel
     * @param address   Morada
     * @param num_cc    Número de cartão de cidadão
     * @param ano_cc    validade do cartão de cidadão (ano)
     * @param mes_cc    validade do cartão de cidadão (mes)
     * @param dia_cc    validade do cartão de cidadão (dia)
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     */
    public boolean insertPerson(String cargo, String pass, int dep, int num_phone, String address, int num_cc, int ano_cc, int mes_cc, int dia_cc) {
        String data = String.format("%d-%d-%d 00:00:00", ano_cc, mes_cc, dia_cc);
        String sql = String.format("INSERT INTO person(job,password,department_id,phone,address,cc_number,cc_validity) VALUES('%s','%s',%s,%s,'%s',%s,'%s')", cargo, pass, dep, num_phone, address, num_cc, data);
        return updateOnDB(sql);
    }

    /**
     * @param anoIni    Inicio da eleição (ano)
     * @param mesIni    Inicio da eleição (mes)
     * @param diaIni    Inicio da eleição (dia)
     * @param horaIni   Inicio da eleição (hora)
     * @param minIni    Inicio da eleição (minuto)
     * @param anoFim    Fim da eleição (ano)
     * @param mesFim    Fim da eleição (mes)
     * @param diaFim    Fim da eleição (dia)
     * @param horaFim   Fim da eleição (hora)
     * @param minFim    Fim da eleição (minuto)
     * @param titulo    Título da eleição
     * @param descricao Breve descrição da eleição
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     */
    public int insertElection(int anoIni, int mesIni, int diaIni, int horaIni, int minIni, int anoFim, int mesFim, int diaFim, int horaFim, int minFim, String titulo, String descricao, String type_ele) {
        String dataIni = String.format("%d-%d-%d %d:%d:00", anoIni, mesIni, diaIni, horaIni, minIni), dataFim = String.format("%d-%d-%d %d:%d:00", anoFim, mesFim, diaFim, horaFim, minFim);
        String sql = String.format("INSERT INTO election(title,type,description,begin_date,end_date) VALUES('%s','%s','%s','%s','%s')", titulo, type_ele, descricao, dataIni, dataFim);
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

    public boolean insertElectionDepartment(int id_election, int id_dep) {
        String sql = String.format("INSERT INTO election_department(election_id,department_id) VALUES(%s,%s)", id_election, id_dep);
        return updateOnDB(sql);
    }

    public boolean insertCandidacyIntoElection(String name, String type, int election_id) {
        return this.updateOnDB(String.format("INSERT INTO candidacy(id,name,type,election_id) VALUES (NULL,'%s','%s',%s);",name,type,election_id));
    }

    public boolean insertPersonIntoCandidacy(int candidacy_id, int cc_number) {
        return this.updateOnDB(String.format("INSERT INTO candidacy_person(candidacy_id,person_cc_number) VALUES (%s,%s);",candidacy_id,cc_number));
    }

    public ArrayList<Election> getElections() {
        return this.selectElections("SELECT * FROM election");
    }

    public ArrayList<Candidacy> getCandidacies(int election_id) {
        return this.selectCandidacies("SELECT * FROM candidacy WHERE election_id = " + election_id);
    }
    public ArrayList<Person> getPeople(int candidacy_id) {
        return this.selectPeople(
                "SELECT *\n" +
                    "FROM person\n" +
                    "WHERE cc_number IN (\n" +
                    "    SELECT person_cc_number\n" +
                    "    FROM candidacy_person\n" +
                    "    WHERE candidacy_id = "+candidacy_id+"\n" +
                    "); "
        );
    }
    public void updateElections(Election e) {
        if (this.updateOnDB(String.format("UPDATE election SET title='%s',type='%s',description='%s',begin_date='%s',end_date='%s' WHERE id=%s", e.getTitle(), e.getType(), e.getDescription(), e.getBegin().toString(), e.getEnd().toString(), e.getId()))){
            System.out.println("Successfully updated election");
        } else {
            System.out.println("Problem updating election");
        }
    }

    public void removeOnDB(String table,String idName ,int id) {
        if (this.updateOnDB("DELETE FROM "+table+" WHERE "+idName+" = "+id)){
            System.out.println("Removed from"+table+" id #"+id);
        }else{
            System.out.println("Problem removing id #"+id+" from database");
        }
    }

    public ArrayList<Department> getDepartments() {
        return this.selectDepartments("SELECT * FROM department");
    }

    public ArrayList<Department> popDepartment(ArrayList<Department> listDep, int id) {
        listDep.remove(id - 1);
        return listDep;
    }
    /**
     * Conexão à base de dados
     *
     * @return conn
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
     * Seleciona eleições na base de dados
     *
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Election> selectElections(String sql) {
        Connection conn = connectDB();
        ArrayList<Election> elections = new ArrayList();
        ;
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
        ArrayList<Candidacy> candidacies = new ArrayList();
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
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Person> selectPeople(String sql) {
        Connection conn = connectDB();
        ArrayList<Person> people = new ArrayList();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                people.add(new Person(
                        rs.getString("address"),
                        rs.getInt("cc_number"),
                        rs.getInt("cc_validity"),
                        rs.getInt("department_id"),
                        rs.getString("job"),
                        rs.getString("password"),
                        rs.getInt("phone")
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
     * @param sql commando sql
     * @return devolve o resultado da query ou null
     */
    public ArrayList<Department> selectDepartments(String sql) {
        Connection conn = connectDB();
        ArrayList<Department> departments = new ArrayList();
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

    public String saySomething() throws RemoteException {
        return "I'm alive!";
    }

    /**
     * @return true ou false caso o servidor secundário consiga enviar com sucesso ou não pings ao servidor primário
     */
    public boolean isPrimaryFunctional() {
        DatagramSocket aSocket = null;
        boolean ok = true;
        try {
            aSocket = new DatagramSocket();
            System.out.print("Mensagem a enviar = ");

            InetAddress aHost = InetAddress.getByName("127.0.0.1");
            int serverPort = 7001;
            byte[] m = "Pinging".getBytes();
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            aSocket.setSoTimeout(200);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
            ok = false;
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
            ok = false;
        } finally {
            if (aSocket != null) aSocket.close();
        }
        return ok;
    }

    /**
     * Inicializa a ligação do Servidor RMI com o Servidor Multicast
     */
    public void initializeRMI() {
        System.out.println("Becoming Primary Server!");
        try {
            RMIServer obj = new RMIServer();
            Registry r = LocateRegistry.createRegistry(7000);
            r.rebind("test", obj);
            r.rebind("admin", obj);
            System.out.println("RMI Server ready!");
        } catch (RemoteException re) {
            System.out.println("Exception in Server.main: " + re);
        }
    }

    /**
     * Inicializa a ligação do servidor primário com o servidor secundário via UDP
     * Necessário para verificar se o servidor primária se mantém ligado, caso contrário o servidor secundário passa a ser primário
     * Quando o servidor primário que deixou de estar funcional se reconectar irá assumir o papel de servidor secundário
     */
    public void initializeUDP() {
        String s;
        try (DatagramSocket aSocket = new DatagramSocket(7001)) {
            System.out.println("Socket Datagram à escuta no porto 7001");
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                s = new String(request.getData(), 0, request.getLength());
                System.out.println("Recebeu um ping: " + s);
                buffer = "I'm Alive".getBytes();
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws RemoteException {
        RMIServer rmiServer = new RMIServer();
        int numPingsFailed = 0;
        while (numPingsFailed < 5) {
            try {
                sleep(200);
                if (!rmiServer.isPrimaryFunctional())
                    numPingsFailed++;
                else numPingsFailed = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        rmiServer.initializeRMI();
        rmiServer.initializeUDP();
    }
}