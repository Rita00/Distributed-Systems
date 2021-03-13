package com.company;

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
     * @param cargo Cargo (Estudante, Docente ou Funcionário)
     * @param pass código de acesso para aceder a uma determinada eleição
     * @param dep Departamento a que a pessoa pertence
     * @param num_phone Número de telemóvel
     * @param address Morada
     * @param num_cc Número de cartão de cidadão
     * @param ano_cc, @param mes_cc, @param dia_cc validade do cartão de cidadão
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     */
    public boolean insertPerson(String cargo, String pass, String dep, int num_phone, String address, int num_cc, int ano_cc, int mes_cc, int dia_cc) {
        int data = ano_cc * 10000 + mes_cc * 100 + dia_cc;
        String sql = String.format("INSERT INTO person(funcao,password,depart,phone,address,numcc,validadecc) VALUES('%s','%s','%s',%s,'%s',%s,%s)", cargo, pass, dep, num_phone, address, num_cc, data);
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
     * Conexão à base de dados
     * @return conn
     */
    static public Connection connectDB() {
        String url = "jdbc:sqlite:Election.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public String saySomething() throws RemoteException {
        return "I'm alive!";
    }

    static public boolean isPrimaryFunctional() {
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

    static public void initializeRMI() {
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

    static public void initializeUDP() {
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

    public static void main(String[] args) {
        int numPingsFailed = 0;
        while (numPingsFailed < 5) {
            try {
                sleep(200);
                if (!isPrimaryFunctional())
                    numPingsFailed++;
                else numPingsFailed = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        initializeRMI();
        initializeUDP();
    }
}