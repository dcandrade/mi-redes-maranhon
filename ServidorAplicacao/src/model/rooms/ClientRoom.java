package model.rooms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;
import model.Book;
import protocols.ClientProtocol;
import util.BooksEngine;
import util.communication.MulticastCentral;
import util.communication.MulticastReceiver;

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
    private final BooksEngine booksEngine;
    private final MulticastCentral mc;
    private final MulticastReceiver mr;
        
        
    
    
    public ClientRoom(Socket client) throws IOException {
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
        this.booksEngine = new BooksEngine();
        mc = new MulticastCentral(5, null,this.booksEngine, true);
        mr = new MulticastReceiver(mc);
        mr.start();
    }

    private void sendMessage(int protocol, String message) throws IOException {
        this.output.writeUTF(protocol + ClientProtocol.SEPARATOR + message);
        this.output.flush();

    }

    private void sendMessage(String message) throws IOException {
        this.output.writeUTF(message);
        this.output.flush();
    }

    private String readMessage() throws IOException {
        return this.input.readUTF();
    }

    @Override
    public void run() {
        try {

            StringTokenizer token = new StringTokenizer(this.readMessage());

            int operation = Integer.parseInt(token.nextToken());

            switch (operation) {
                case ClientProtocol.SHOWMETHEBOOKS:
                    LinkedList<Book> books = this.booksEngine.getBooks();
                    StringBuilder builder = new StringBuilder();

                    for (Book book : books) {
                        builder.append(book.serialize(ClientProtocol.SEPARATOR));
                    }

                    this.sendMessage(builder.toString());
                    break;
                case ClientProtocol.GIVEMETHEBOOKS:
                    String name = token.nextToken();
                    int amount = Integer.parseInt(token.nextToken());
                    if(booksEngine.getAmount(name) >= amount){
                        while(booksEngine.turnOnSemaphore(name)){
                            int packetID = mc.createPacket(9, "turnOnSemaphore");
                            mc.send(packetID);//envia requisição para alterar semaforo de outros servidores
                            //trying to make sempahore on, when it's on server will be able to write on file
                        }
                        booksEngine.decreaseAmount(name, amount);//efetua a escrita
                        int newAmount = booksEngine.getAmount(name);//nova quantidade de livros
                        this.sendMessage("true");
                        //envia alterações (name,newAmount)
                        //envia requisição para alterar o semaforo dos servidores
                        booksEngine.turnOffSemaphore(name);
                        break;
                    }
                    this.sendMessage("false");
                    break;
            }
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Erro em ClientRoom \n" + ex);
        }
    }

}
