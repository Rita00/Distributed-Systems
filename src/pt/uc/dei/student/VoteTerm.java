package pt.uc.dei.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Clientes do Multicast Server (Terminais de Voto)
 */
public class VoteTerm extends Thread {
    private final int PORT;
    private final String MULTICAST_ADDRESS;
    
    VoteTerm(String multicastAddress, int port){
    	this.MULTICAST_ADDRESS=multicastAddress;
    	this.PORT=port;
    }

    public void run() {
        try (MulticastSocket socket = new MulticastSocket(this.PORT)) {
            InetAddress group = InetAddress.getByName(this.MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message::");
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VoteTerm client = new VoteTerm("224.3.2.1",7001);
        client.start();
    }
}
