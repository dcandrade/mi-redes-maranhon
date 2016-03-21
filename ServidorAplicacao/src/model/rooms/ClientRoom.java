package model.rooms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Handler;
import model.Book;
import protocols.ClientProtocol;
import util.BooksEngine;
import util.communication.MulticastCentral;
import util.communication.MulticastReceiver;
import util.communication.ServerRequestHandler;

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

    public ClientRoom(Socket client, int id) throws IOException {
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
        mc = new MulticastCentral(id, true);
        mr = new MulticastReceiver(mc);
        this.booksEngine = BooksEngine.getInstance(mc);
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
                    
                    Boolean result = this.booksEngine.decreaseAmount(name, amount);
                    this.sendMessage(result.toString());
                    break;
            }
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Erro em ClientRoom \n" + ex);
        }
    }

}
