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
import util.LoginEngine;
import util.ShoppingLog;
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
    private final BooksEngine books;
    private final LoginEngine login;
    private final ShoppingLog shopping;

    public ApplicationServer() throws IOException {
        this.books = new BooksEngine();
        this.login = new LoginEngine();
        this.shopping = new ShoppingLog();
    }
    
    private void setUpConnection() throws IOException {
        ServerRequestHandler handler = new ServerRequestHandler(this.books, this.shopping, this.login);
        this.mc = new MulticastCentral(id, handler, true);
        this.mr = new MulticastReceiver(this.mc);
        this.mr.start();
        this.books.setMulticastCentral(this.mc);
        this.login.setMulticastCentral(mc);
        this.shopping.setMulticastCentral(mc);
        handler.setMulticastCentral(mc);

        int packet = mc.createPacket(ServerProtocol.NEW_SERVER, null);
        mc.send(packet);
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

    public void start() throws IOException {
        String request = CentralizerProtocol.START_SERVER
                + CentralizerProtocol.SEPARATOR + this.port;
                
        this.output.writeUTF(request);
        this.output.flush();
        this.output.writeInt(this.port);
        this.output.flush();
    }

    public void listenClients(int id, int port) throws IOException {
        ClientListener listener = new ClientListener(id, port, this.books, this.shopping, this.login);
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

    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationServer app = new ApplicationServer();
        app.connectAsServer();

        System.out.println("Configurando...");
       Thread.sleep(2000);
        System.out.println("Feito.");
        
        app.start();
        app.listenClients(app.getId(), app.getPort());
        System.out.println("Iniciado servidor " + app.getId() + " em " + app.getPort());
    }
}
