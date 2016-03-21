/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.listeners;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import model.rooms.ClientRoom;

/**
 *
 * @author dcandrade
 */
public class ClientListener implements Runnable{
        private final ServerSocket server;
        private final int serverID;

    public ClientListener(int serverID, int port) throws IOException {
        this.server = new ServerSocket(port);
        this.serverID=serverID;
    }
            
    @Override
    public void run() {
        ClientRoom room;
        Thread thread;
        while(true){
            try {
                Socket client = server.accept();
                System.out.println("Cliente " + client.getInetAddress().getHostAddress() + "se conectou.");
                room = new ClientRoom(client, serverID);
                thread = new Thread(room);
                thread.start();
            } catch (IOException ex) {
                System.err.println("Erro em Client Listener:");
                ex.printStackTrace();
            }
        }
    }
}
