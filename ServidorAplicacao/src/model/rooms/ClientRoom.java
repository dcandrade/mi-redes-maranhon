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

    private final DataInputStream input;
    private final DataOutputStream output;

    public ClientRoom(Socket client) throws IOException {
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
    }

    private void sendMessage(int protocol, String message) throws IOException {
        this.output.writeUTF(protocol + ClientProtocol.SEPARATOR + message);
    }

    private String readMessage() throws IOException {
        return this.input.readUTF();
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
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Erro em ClientRoom \n" + ex);
        }
    }

}
