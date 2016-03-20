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

/**
 *
 * @author dcandrade
 */
public class ApplicationServer {
    private static int ID = 1;
    private DataInputStream input;
    private DataOutputStream output;
    private final int id;
    private int port;
    
    public ApplicationServer(){
        this.id = ApplicationServer.ID++;
    }
    
    public void connectAsServer() throws IOException{
        Socket socket = new Socket(CentralizerProtocol.IP, CentralizerProtocol.PORT);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        
        output.writeInt(CentralizerProtocol.IM_A_SERVER);
        this.port = input.readInt();
    }
    
    public void listenClients(int id, int port) throws IOException{
        ClientListener listener = new ClientListener(id, port);
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
        System.out.println("Iniciando servidor "+app.getId()+" em "+app.getPort());
        app.listenClients(app.getId(), app.getPort());
        System.out.println("Servidor iniciado.");
    }
}
