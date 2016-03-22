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
import util.BooksEngine;

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

    public MulticastCentral(int id, boolean debug) throws UnknownHostException, IOException {
        this.address = InetAddress.getByName(ServerProtocol.MULTICAST_ADDRESS);
        this.id = id;
        this.packets = new TreeMap<>();
        this.cache = new TreeMap<>();
        this.handler = new ServerRequestHandler();
        this.debug = debug;
    }

    public int createPacket(int protocolID, String message) {
        if (debug) {
            System.err.println("::Creating packet::");
            System.err.println("Properties:");
            System.err.println("Type: " + ServerProtocol.SERVER_STUFF);
            System.err.println("Sender ID: " + this.id);
            System.err.println("Packet Number: " + MulticastCentral.packetNumber);
            System.err.println("Application Protocol: " + protocolID);
            System.err.println("Message: " + message);
            System.err.println(":::");
        }
        StringBuilder packet = new StringBuilder();
        packet.append(ServerProtocol.SERVER_STUFF).append(ServerProtocol.SEPARATOR);
        packet.append(this.id).append(ServerProtocol.SEPARATOR);
        packet.append(MulticastCentral.packetNumber).append(ServerProtocol.SEPARATOR);
        packet.append(protocolID).append(ServerProtocol.SEPARATOR);
        packet.append(message);

        this.packets.put(MulticastCentral.packetNumber, packet.toString());
        if (debug) {
            System.out.println("Packet created: " + packet.toString());
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
        System.err.println("Enviando pacote: "+packet);
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = packet.getBytes();
        DatagramPacket dpacket = new DatagramPacket(buffer, buffer.length, this.address, MulticastCentral.PORT);

        socket.send(dpacket);
    }

    public void send(int id) throws IOException {
        if (debug) {
            System.out.println("Sending packet n" + id);
        }

        this.send(this.packets.get(id));
        this.watchPacket(id);
    }

    public void processPacket(String packet) throws IOException {
        if (debug) {
            System.err.print("Recebido: " + packet);
        }
        StringTokenizer tokenizer = new StringTokenizer(packet, ServerProtocol.SEPARATOR);

        int operation = Integer.parseInt(tokenizer.nextToken());
        int sender = Integer.parseInt(tokenizer.nextToken());
        int packetID = Integer.parseInt(tokenizer.nextToken());

        if (sender == this.id) {
            if (debug) {
                System.err.println(" ->Pacote descartado");
            }
            return;
        }

        if (debug) {
            System.err.print("\nTipo: " + operation + ", ");
            System.err.print("Remetente: " + sender + ", ");
            System.err.println("ID do Pacote: " + packetID);

        }

        if (operation == ServerProtocol.MULTICAST_STUFF) {
            int protocol = Integer.parseInt(tokenizer.nextToken());

            if (debug) {
                System.err.print("Operacao de Multicast: ");
                System.err.println("Nº de Protocolo: " + protocol);
            }

            if (protocol == ServerProtocol.CONFIRMATION) {
                if (debug) {
                    System.out.println("Pacote " + packetID + " confirmado."
                            + " Enviando reconfirmação");
                }

                this.markPacketAsReceived(packetID);
                this.sendReconfirmation(packetID, this.id);

            } else if (protocol == ServerProtocol.RECONFIRMATION) {
                System.err.println("Transação do pacote " + packetID
                        + " concluída com sucesso");
                this.endPacketTransaction(packetID, sender);
            }

        } else if (operation == ServerProtocol.SERVER_STUFF) {
            //Só processa pacotes que não foram processados antes
            if (cache.get(sender) == null) {
                this.cache.put(sender, new TreeMap<Integer, String>());
            }
            if (cache.get(sender).get(packetID) != null) {
                System.err.println(cache.get(sender).get(packetID));
                System.err.println("Pacote já foi processado");
                this.sendConfirmation(packetID, sender);
                return;
            } else {
                this.cache.get(sender).put(packetID, packet);
            }

            if (debug) {
                System.err.println("Operação de Servidor, enviando confirmação.");
            }

            this.markPacketAsReceived(packetID);
            this.sendConfirmation(packetID, this.id);
            this.waitReconfirmation(id);
            
            
            StringBuilder request = new StringBuilder();
            while (tokenizer.hasMoreTokens()) {
                request.append(tokenizer.nextToken()).append(ServerProtocol.SEPARATOR);
            }
            
            this.handler.processRequest(request.toString());
        }
    }

    private void waitReconfirmation(int packetID) {
        if (debug) {
            System.err.println("Waiting reconfirmation...");
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
