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
import util.BooksEngine;
import util.LoginEngine;
import util.ShoppingLog;

/**
 *
 * @author dcandrade
 */
public class ClientListener implements Runnable{
        private final ServerSocket server;
        private final int serverID;
        private final BooksEngine books;
        private final LoginEngine login;
        private final ShoppingLog log;

    public ClientListener(int serverID, int port, BooksEngine books, ShoppingLog log, LoginEngine login) throws IOException {
        this.server = new ServerSocket(port);
        this.serverID=serverID;
        this.books = books;
        this.login = login;
        this.log = log;
    }
            
    @Override
    public void run() {
        ClientRoom room;
        Thread thread;
        while(true){
            try {
                Socket client = this.server.accept();
                System.out.println("Cliente " + client.getInetAddress().getHostAddress() + "se conectou.");
                room = new ClientRoom(client, this.serverID, this.books, this.log, this.login);
                thread = new Thread(room);
                thread.start();
            } catch (IOException ex) {
                System.err.println("Erro em Client Listener:");
                ex.printStackTrace();
            }
        }
    }
}
