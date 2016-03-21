/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import protocols.ServerProtocol;

/**
 *
 * @author dcandrade
 */
public class MulticastReceiver extends Thread {

    private static final int PORT = 8888;
    private final MulticastSocket socket;
    private final MulticastCentral sender;

    public MulticastReceiver(MulticastCentral sender) throws IOException {
        this.socket = new MulticastSocket(PORT);
        InetAddress address = InetAddress.getByName(ServerProtocol.MULTICAST_ADDRESS);
        this.socket.joinGroup(address);
        this.sender = sender;
    }
    
    @Override
    public void run() {
        byte[] inBuffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
        String message;

        while (true) {
            try {
                this.socket.receive(packet);
                message = new String(inBuffer, 0, packet.getLength());
                //System.out.println("Mensagem Recebida: "+ message);
                this.sender.processPacket(message);
                            
            } catch (IOException ex) {
                //TODO
            }
        }

    }
}
