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
    private final TreeMap<Integer, TreeMap<Integer, String>> cache;
    private final ServerRequestHandler handler;
    private final boolean debug;
    public static final boolean RELIABLE = false;

    public MulticastCentral(int id, ServerRequestHandler handler, boolean debug) throws UnknownHostException, IOException {
        this.address = InetAddress.getByName(ServerProtocol.MULTICAST_ADDRESS);
        this.id = id;
        this.packets = new TreeMap<>();
        this.cache = new TreeMap<>();
        this.handler = handler;
        this.debug = debug;
    }

    public int createPacket(String operation, String message) {

        StringBuilder packet = new StringBuilder();
        packet.append(ServerProtocol.SERVER_STUFF).append(ServerProtocol.SEPARATOR);
        packet.append(this.id).append(ServerProtocol.SEPARATOR);
        packet.append(MulticastCentral.packetNumber).append(ServerProtocol.SEPARATOR);
        packet.append(operation).append(ServerProtocol.SEPARATOR);
        packet.append(message);

        this.packets.put(MulticastCentral.packetNumber, packet.toString());

        if (debug) {
            System.out.println("::[COMMUNICATION] PACKET CREATED::");
            System.out.println("-Properties:");
            System.out.println("-Type: " + ServerProtocol.SERVER_STUFF);
            System.out.println("-Sender ID: " + this.id);
            System.out.println("-Packet Number: " + MulticastCentral.packetNumber);
            System.out.println("-Message Type: " + operation);
            System.out.println("-Message: " + message);
            System.out.println("::::::::::::::::::\n");
        }

        return MulticastCentral.packetNumber++;
    }

    private void markPacketAsReceived(int id) {
        this.packets.put(id, ServerProtocol.RECEIVED);
    }

    private void endPacketTransaction(int id, int sender) {
        this.packets.remove(id);
        this.cache.get(sender).remove(id);
    }

    public boolean isTransactionFinished(int id) {
        return this.packets.get(id) == null;
    }

    private void watchPacket(int id) {
        PacketWatcher watcher = new PacketWatcher(id, this);
        Thread t = new Thread(watcher);
        t.start();
    }

    public boolean isReceived(int id) {
        return this.packets.get(id).equals(ServerProtocol.RECEIVED);
    }

    public void resendPacket(int id) throws IOException {
        this.send(this.packets.get(id));
    }

    public void sendConfirmation(int idPacket, int sender) throws IOException {
        String packet = ServerProtocol.MULTICAST_STUFF + ServerProtocol.SEPARATOR
                + sender + ServerProtocol.SEPARATOR
                + idPacket + ServerProtocol.SEPARATOR
                + ServerProtocol.CONFIRMATION;

        PacketWatcher watcher = new PacketWatcher(id, sender, this);
        this.send(packet);
        Thread t = new Thread(watcher);
        t.start();
    }

    private void send(String packet) throws SocketException, IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = packet.getBytes();
        DatagramPacket dpacket = new DatagramPacket(buffer, buffer.length, this.address, MulticastCentral.PORT);

        socket.send(dpacket);
    }

    public void send(int id) throws IOException {
        if (debug) {
            System.out.println("[COMMUNICATION] Sending packet " + id);
        }

        this.send(this.packets.get(id));

        if (MulticastCentral.RELIABLE) {
            this.watchPacket(id);
        }
    }

    public void processPacket(String packet) throws IOException {

        StringTokenizer tokenizer = new StringTokenizer(packet, ServerProtocol.SEPARATOR);

        String operation = tokenizer.nextToken();
        int sender = Integer.parseInt(tokenizer.nextToken());
        int packetID = Integer.parseInt(tokenizer.nextToken());

        if (debug) {
            System.out.println("\n::[COMMUNICATION] PACKET: " + packet + " RECEIVED::");
            System.out.println("-Message Type: " + operation);
            System.out.println("-Sender: " + sender);
            System.out.println("-Packet ID: " + packetID);
            System.out.println(":::::::\n");
        }

        if (sender == this.id) {
            if (debug) {
                System.out.println("[COMMUNICATION] packet " + packetID + " discarded");
            }
            return;
        }

        switch (operation) {
            case ServerProtocol.MULTICAST_STUFF:
                String protocol = tokenizer.nextToken();
                if (debug) {
                    System.out.print("[COMMUNICATION] MULTICAST OPERATION: ");
                    System.out.println("Type: " + protocol);
                }
                if (protocol.equals(ServerProtocol.CONFIRMATION) && MulticastCentral.RELIABLE) {
                    if (debug) {
                        System.out.println("[COMMUNICATION] Packet " + packetID + " confimed."
                                + " SENDING RECONFIRMATION...");
                    }

                    this.markPacketAsReceived(packetID);
                    this.sendReconfirmation(packetID, this.id);

                } else if (protocol.equals(ServerProtocol.RECONFIRMATION) && MulticastCentral.RELIABLE) {
                    System.out.println("[COMMUNICATION] Transaction of packet " + packetID
                            + " was sucessfully completed.");
                    this.endPacketTransaction(packetID, sender);
                }
                break;
            case ServerProtocol.SERVER_STUFF:
                //Só processa pacotes que não foram processados antes
                if (cache.get(sender) == null) {
                    this.cache.put(sender, new TreeMap<Integer, String>());
                }
                if (cache.get(sender).get(packetID) != null) {
                    System.out.println("[COMMUNICATION] PACKET " + packet + " ALREADY PROCESSED.");
                    this.sendConfirmation(packetID, sender);
                    return;
                } else {
                    this.cache.get(sender).put(packetID, packet);
                }
                if (debug) {
                    System.out.print("[COMMUNICATION] APPLICATION OPERATION");
                }
                if (MulticastCentral.RELIABLE) {
                    System.out.print(", sending confirmation...");
                    this.markPacketAsReceived(packetID);
                    this.sendConfirmation(packetID, this.id);
                    this.waitReconfirmation(id);
                }
                System.out.print("\n");
                StringBuilder request = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    request.append(tokenizer.nextToken()).append(ServerProtocol.SEPARATOR);
                }
                this.handler.processRequest(request.toString());
                break;
        }
    }

    private void waitReconfirmation(int packetID) {
        if (debug) {
            System.out.println("[COMMUNICATION] Waiting reconfirmation of packet " + packetID + "...");
        }
        PacketWatcher watcher = new PacketWatcher(packetID, this.id, this);
        watcher.watch();
    }

    private void sendReconfirmation(int idPacket, int sender) throws IOException {
        String packet = ServerProtocol.MULTICAST_STUFF + ServerProtocol.SEPARATOR
                + sender + ServerProtocol.SEPARATOR
                + idPacket + ServerProtocol.SEPARATOR
                + ServerProtocol.RECONFIRMATION;
        this.send(packet);
    }
}
