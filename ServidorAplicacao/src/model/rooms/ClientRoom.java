package model.rooms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import protocols.ClientProtocol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kayo
 */
public class ClientRoom implements Runnable {

    private DataInputStream dis;
    private final DataOutputStream dos;

    public ClientRoom(Socket client) throws IOException {
        this.dis = new DataInputStream(client.getInputStream());
        this.dos = new DataOutputStream(client.getOutputStream());
    }

    private void sendMessage(int protocol, String message) throws IOException {
        this.dos.writeUTF(protocol + ClientProtocol.SEPARATOR + message);
    }

    private String readMessage() throws IOException {
        return this.dis.readUTF();
    }

    @Override
    public void run() {
        try {
            while (true) {
                StringTokenizer token = new StringTokenizer(this.readMessage());
                
                int operation = Integer.parseInt(token.nextToken());
                
                switch(operation){
                    //tratar as requisições do cliente
                }
            }
        } catch (Exception ex) {
            System.err.println("Erro em ClientRoom \n" + ex);
        }
    }

}
