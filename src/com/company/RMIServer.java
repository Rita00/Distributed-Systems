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

import static java.lang.Thread.sleep;

public class RMIServer extends UnicastRemoteObject implements RMI {
    public RMIServer() throws RemoteException {
        super();
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
