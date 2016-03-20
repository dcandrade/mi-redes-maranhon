/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import protocols.ServerProtocol;

/**
 *
 * @author dcandrade
 */
public class MulticastCentral {

    private static final int PORT = 8888;
    private final InetAddress address;
    private final int id;
    private static int packetNumber = 1;
    private final TreeMap<Integer, String> packets;
    private final ServerRequestHandler handler;

    public MulticastCentral(int id, ServerRequestHandler handler) throws UnknownHostException {
        this.address = InetAddress.getByName(ServerProtocol.MULTICAST_ADDRESS);
        this.id = id;
        this.packets = new TreeMap<>();
        this.handler = handler;
    }

    public int createPacket(int protocolID, String message) {
        System.out.println("::Creating packet::");
        System.out.println("Properties:");
        System.out.println("Type: "+ServerProtocol.SERVER_STUFF);
        System.out.println("Sender ID: " + this.id);
        System.out.println("Packet Number: "+MulticastCentral.packetNumber);
        System.out.println("Application Protocol: " + protocolID);
        System.out.println("Message: " + message);
        System.out.println(":::");
        StringBuilder packet = new StringBuilder();
        packet.append(ServerProtocol.SERVER_STUFF).append(ServerProtocol.SEPARATOR);
        packet.append(this.id).append(ServerProtocol.SEPARATOR);
        packet.append(MulticastCentral.packetNumber).append(ServerProtocol.SEPARATOR);
        packet.append(protocolID).append(ServerProtocol.SEPARATOR);
        packet.append(message);

        this.packets.put(MulticastCentral.packetNumber, packet.toString());
        System.out.println("Packet created: " + packet.toString());
        return MulticastCentral.packetNumber++;
    }

    private void markPacketAsReceived(int id) {
        this.packets.put(id, ServerProtocol.RECEIVED);
    }

    private void endPacketTransaction(int id) {
        this.packets.remove(id);
    }

    public boolean isTransactionFinished(int id) {
        return this.packets.get(id) == null;
    }

    private void watchPacket(int id) {
        PacketWatcher watcher = new PacketWatcher(id);
        watcher.watch();
    }

    public boolean isReceived(int id) {
        return this.packets.get(id).equals(ServerProtocol.RECEIVED);
    }

    public void resendPacket(int id) throws IOException {
        this.send(this.packets.get(id));
    }

    public void sendConfirmation(int idPacket, int sender) throws IOException {
        String packet = ServerProtocol.MULTICAST_STUFF + ServerProtocol.SEPARATOR
                + ServerProtocol.CONFIRMATION + ServerProtocol.SEPARATOR
                + idPacket + ServerProtocol.SEPARATOR + sender;
        this.send(packet);
    }

    private void send(String packet) throws SocketException, IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = packet.getBytes();
        DatagramPacket dpacket = new DatagramPacket(buffer, buffer.length, this.address, MulticastCentral.PORT);

        socket.send(dpacket);
    }

    public void send(int id) throws IOException {
        System.out.println("Sending packet n" + id);
        this.send(this.packets.get(id));
    }

    public void processPacket(String packet) throws IOException {
        System.out.println("Packet received");
        StringTokenizer tokenizer = new StringTokenizer(packet, ServerProtocol.SEPARATOR);
        tokenizer.nextToken(); //Discards the destiny

        int operation = Integer.parseInt(tokenizer.nextToken());
        if (operation == ServerProtocol.MULTICAST_STUFF) {
            int idPacket;
            int sender;
            switch (operation) {
                case ServerProtocol.CONFIRMATION:
                    idPacket = Integer.parseInt(tokenizer.nextToken());
                    sender = Integer.parseInt(tokenizer.nextToken());
                    this.markPacketAsReceived(idPacket);
                    this.sendReconfirmation(idPacket, sender);
                    break;
                case ServerProtocol.RECONFIRMATION:

                    idPacket = Integer.parseInt(tokenizer.nextToken());
                    this.endPacketTransaction(idPacket);
                    System.out.println("Transaction " + idPacket + " completed");
                    break;
            }
        } else if (operation == ServerProtocol.SERVER_STUFF) {
            int idPacket = Integer.parseInt(tokenizer.nextToken());
            int sender = Integer.parseInt(tokenizer.nextToken());
            this.sendConfirmation(idPacket, sender);
            this.waitReconfirmation(idPacket, sender);

            if (tokenizer.hasMoreTokens()) {
                StringBuilder builder = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    builder.append(tokenizer.nextToken()).append(ServerProtocol.SEPARATOR);
                }
                this.handler.processRequest(builder.toString());
            }
        }

    }

    private void waitReconfirmation(int id, int idSender) {
        System.out.println("Waiting reconfirmation...");
        PacketWatcher watcher = new PacketWatcher(id, idSender);
        watcher.watch();
    }

    private void sendReconfirmation(int idPacket, int sender) throws IOException {
        System.out.println("Sending reconfirmation...");
        String packet = ServerProtocol.MULTICAST_STUFF + ServerProtocol.SEPARATOR
                + ServerProtocol.RECONFIRMATION + ServerProtocol.SEPARATOR
                + idPacket + ServerProtocol.SEPARATOR + sender;
        this.send(packet);
    }
}
