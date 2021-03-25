package pt.uc.dei.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;

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
               this.parseMessage(message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String,String> parseMessage(String msg){
        HashMap<String,String> hash = new HashMap<String,String>();
        String[] dividedMessage = msg.split(" ; ");
        for(String token : dividedMessage){
            String[] keyVal = token.split(" | ");
            if(keyVal.length == 2){
                hash.put(keyVal[0], keyVal[1]);
            }else{
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }

    public static void main(String[] args) {
        VoteTerm client = new VoteTerm("224.3.2.1",MulticastServer.MULTICAST_PORT);
        client.start();
    }
}
