package pt.uc.dei.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
    public boolean insertPerson(String cargo, String pass, String dep, int num_phone, String address, int num_cc, int ano_cc, int mes_cc, int dia_cc) {
        int data = ano_cc * 10000 + mes_cc * 100 + dia_cc;
        String sql = String.format("INSERT INTO person(funcao,password,depart,phone,address,numcc,validadecc) VALUES('%s','%s','%s',%s,'%s',%s,%s)", cargo, pass, dep, num_phone, address, num_cc, data);
        return insertOnDB(sql);
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
    public boolean insertElection(int anoIni, int mesIni, int diaIni, int horaIni, int minIni, int anoFim, int mesFim, int diaFim, int horaFim, int minFim, String titulo, String descricao) {
        long dataIni = anoIni * 100000000L + mesIni * 1000000L + diaIni * 10000L + horaIni * 100L + minIni, dataFim = anoFim * 100000000L + mesFim * 1000000L + diaFim * 10000L + horaFim * 10L + minFim;
        String sql = String.format("INSERT INTO election(inidate,fimdate,title,description) VALUES(%s,%s,'%s','%s')", dataIni, dataFim, titulo, descricao);
        return insertOnDB(sql);
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
     * Insere na base de dados na respetivas tabela
     * @param sql commando sql
     * @return true ou false dependendo se a inserção teve ou não sucesso
     */
    public boolean insertOnDB(String sql) {
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