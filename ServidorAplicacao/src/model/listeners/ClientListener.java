/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.listeners;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.rooms.ClientRoom;
import protocols.ClientProtocol;

/**
 *
 * @author dcandrade
 */
public class ClientListener implements Runnable{
        private final ServerSocket server;
        private final int id;
        private final static Random random = new Random();

    public ClientListener(int id, int port) throws IOException {
        //this.server = new ServerSocket(ClientProtocol.PORT);
        this.server = new ServerSocket(port);
        this.id=id;
    }
            
    @Override
    public void run() {
        ClientRoom room;
        Thread thread;
        while(true){
            try {
                Socket client = server.accept();
                System.out.println("Cliente conectado no servidor" + this.id);
                room = new ClientRoom(client);
                thread = new Thread(room);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
