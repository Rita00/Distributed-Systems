package pt.uc.dei.student;

import pt.uc.dei.student.elections.Department;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private final String MULTICAST_ADDRESS = "224.3.2.1";
    public final static int MULTICAST_PORT = 7002;
    private final long SLEEP_TIME = 5000;
    private boolean isON = true;

    private int multicastId;
    private RMI rmiServer;
    private Department department;

    static MulticastServer multicastServer;

    private final NotifierCallBack NOTIFIER = new NotifierCallBack();

    public MulticastServer(RMI rmiServer) throws RemoteException {
        this.rmiServer = rmiServer;
    }

    private void connect() throws InterruptedException {
        String depName = this.department.getName();
        if (depName != null) {
            System.out.printf("======== Mesa de Voto #%s (%s) ========%n", this.getMulticastId(), depName);
            while (true) {
                System.out.println("HelloClient: ");
                sleep(1000);
            }
        }
    }

    public void run() {
        long counter = 0;
        try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
            //O servidor não recebe mensagens dos clientes (sem o Port)
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group); // Para o servidor receber mensagens dar join ao grupo
            while (isON) {
                String message = String.format("sender | multicast%s ; department | %s ; message | hello", this.getMulticastId(), this.department.getId());
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
                socket.send(packet);
                /*
                RECEBER E PARSE DO PACOTE
                 */
                socket.receive(packet);
                HashMap<String, String> msgHash = this.parseMessage(new String(packet.getData(), 0, packet.getLength()));
                /*
                USAR A INFORMACOES DO PACOTE
                 */
                this.doThings(msgHash);

                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doThings(HashMap<String, String> msgHash) {
        //nao ler as suas proprias mensagens
        if (!msgHash.get("sender").equals("multicast" + this.getMulticastId())) {
            System.out.println(msgHash.get("message"));
        }
    }

    private HashMap<String, String> parseMessage(String msg) {
        HashMap<String, String> hash = new HashMap<String, String>();
        String[] dividedMessage = msg.split(" ; ");
        for (String token : dividedMessage) {
            String[] keyVal = token.split(" \\| ");
            if (keyVal.length == 2) {
                hash.put(keyVal[0], keyVal[1]);
            } else {
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }

    public void listDepart(ArrayList<Department> departments) {
        for (Department dep : departments) {
            System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
        }
    }


    public int getMulticastId() {
        return this.multicastId;
    }

    public void setMulticastId(int multicastId) {
        this.multicastId = multicastId;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void reconnectToRMI() {
        while (true) {
            try {
                RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                multicastServer = new MulticastServer(rmiServer);
                break;
            } catch (RemoteException | NotBoundException remoteException) {
                remoteException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            int dep = -1;
            Scanner input = new Scanner(System.in);
            RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
            multicastServer = new MulticastServer(rmiServer);
            /*
            SETUP
             */
            ArrayList<Department> departments = multicastServer.rmiServer.getDepartments();
            while (!(dep >= 1 && dep <= 11)) {
                System.out.println("Departamento onde se localiza: ");
                multicastServer.listDepart(departments);
                System.out.print(">>> ");
                dep = input.nextInt();
            }
            if (rmiServer.initializeMulticast(dep, multicastServer.NOTIFIER) != null) {
                multicastServer.setMulticastId(dep);
                multicastServer.setDepartment(departments.get(dep - 1));
            /*
            LIGAR
             */
                multicastServer.start();
                multicastServer.connect();
            }
            System.exit(0);
        } catch (Exception e) {
            while (true) {
                try {
                    RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                    multicastServer = new MulticastServer(rmiServer);
                    break;
                } catch (RemoteException | NotBoundException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        }
    }
}
