package pt.uc.dei.student;

import pt.uc.dei.student.elections.Department;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_RMI = 7000;
    private int PORT_MULTICAST = 7001;
    private long SLEEP_TIME = 5000;

    private RMI rmiServer;

    public MulticastServer(RMI rmiServer) {
        this.rmiServer = rmiServer;
    }

    public void run() {
        long counter = 0;
        try (MulticastSocket socket = new MulticastSocket(PORT_MULTICAST)) {
            //O servidor não recebe mensagens dos clientes (sem o Port)
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group); // Para o servidor receber mensagens dar join ao grupo
            while (true) {
                String message = this.getName() + "packet " + counter++;
                byte[] buffer = message.getBytes();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_MULTICAST);
                socket.send(packet);
                socket.receive(packet); // Para o servidor receber mensagens
                System.out.println(message);
                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listDepart() {
        try {
            ArrayList<Department> departments;
            departments = this.rmiServer.getDepartments();
            for (Department dep : departments) {
                System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Works like a client of the RMI Server
        int dep = -1;
        try {
            Scanner input = new Scanner(System.in);
            RMI h = (RMI) LocateRegistry.getRegistry(7000).lookup("clientMulticast");
            MulticastServer multicastServer = new MulticastServer(h);
            while (!(dep >= 1 && dep <= 11)) {
                System.out.println("Departamento onde se localiza: ");
                multicastServer.listDepart();
                System.out.print(">>> ");
                dep = input.nextInt();
            }
            String name_dep = h.initializeMulticast(dep);
            if (name_dep != null) {
                System.out.printf("======== Mesa de Voto do %s ========%n", name_dep);
                //do stuff
                //h.listDepart();
                while (true) {
                    //String message = h.saySomething();
                    //System.out.println("HelloClient: " + message);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

        // Works like a server of the voting terminals
        /*MulticastServer server = new MulticastServer();
        server.start();
        try {
            sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
