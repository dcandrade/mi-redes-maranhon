/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import model.listeners.ClientListener;
import protocols.CentralizerProtocol;
import protocols.ServerProtocol;
import util.BooksEngine;
import util.communication.MulticastCentral;
import util.communication.MulticastReceiver;
import util.communication.ServerRequestHandler;

/**
 *
 * @author dcandrade
 */
public class ApplicationServer {

    private DataInputStream input;
    private DataOutputStream output;
    private int id;
    private int port;
    private MulticastCentral mc;
    private MulticastReceiver mr;
    private BooksEngine books;

    private void setUpConnection() throws IOException {
        this.books = new BooksEngine();
        ServerRequestHandler handler = new ServerRequestHandler(books);
        this.mc = new MulticastCentral(id, handler, true);
        this.mr = new MulticastReceiver(this.mc);
        this.mr.start();
        this.books.setMulticastCentral(this.mc);
        handler.setMulticastCentral(mc);
        int createPacket = mc.createPacket(ServerProtocol.NEW_SERVER, null);
        mc.send(createPacket);
    }

    public void connectAsServer() throws IOException {
        Socket socket = new Socket(CentralizerProtocol.IP, CentralizerProtocol.PORT);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());

        output.writeUTF(CentralizerProtocol.IM_A_SERVER);
        this.port = input.readInt();
        this.id = input.readInt();
        this.setUpConnection();
        
    }

    public void listenClients(int id, int port) throws IOException {
        ClientListener listener = new ClientListener(id, port, this.books);
        // Heartbeat heartbeat = new Heartbeat(dos);
        //heartbeat.beat();
        listener.run();
    }

    public int getId() {
        return this.id;
    }

    public int getPort() {
        return port;
    }

    public static void main(String[] args) throws IOException {
        ApplicationServer app = new ApplicationServer();
        app.connectAsServer();
        System.out.println("Iniciando servidor " + app.getId() + " em " + app.getPort());
        app.listenClients(app.getId(), app.getPort());
        System.out.println("Servidor iniciado.");
    }
}
