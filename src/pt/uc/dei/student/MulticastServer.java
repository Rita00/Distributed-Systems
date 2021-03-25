package pt.uc.dei.student;

import pt.uc.dei.student.elections.Department;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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

    private int id;
    private RMI rmiServer;
    private Department department;

    private final NotifierCallBack NOTIFIER = new NotifierCallBack();

    public MulticastServer(RMI rmiServer) throws RemoteException {
        this.rmiServer = rmiServer;
    }

    private void connect() throws InterruptedException {
        String depName =  this.department.getName();
        if (depName != null) {
            System.out.printf("======== Mesa de Voto #%s (%s) ========%n",this.id ,depName);
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
                String message = String.format("sender | %s ; department | %s ; message | hello", this.getName(), this.department.getId());
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
                socket.send(packet);
                socket.receive(packet); // Para o servidor receber mensagens
                if(packet.getLength()>0){
                    this.parseMessage(new String(packet.getData(),0,packet.getLength()));
                }
                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException ignored) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String,String> parseMessage(String msg){
        System.out.println(msg);
        HashMap<String,String> hash = new HashMap<String,String>();
        String[] dividedMessage = msg.split(" ; ");
        for(String token : dividedMessage){
            String[] keyVal = token.split(" \\| ");
            if(keyVal.length == 2){
                hash.put(keyVal[0], keyVal[1]);
            }else{
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }

    public void listDepart( ArrayList<Department> departments) {
        for (Department dep : departments) {
            System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
        }
    }



    public void setId(int id){
        this.id=id;
    }
    public void setDepartment(Department department){
        this.department=department;
    }

    public static void main(String[] args) {
        try {
            int dep = -1;
            Scanner input = new Scanner(System.in);
            RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
            MulticastServer multicastServer = new MulticastServer(rmiServer);
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
            rmiServer.initializeMulticast(dep, multicastServer.NOTIFIER);
            multicastServer.setId(dep);
            multicastServer.setDepartment(departments.get(dep-1));
            /*
            LIGAR
             */
            multicastServer.start();
            multicastServer.connect();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }
    }
}
