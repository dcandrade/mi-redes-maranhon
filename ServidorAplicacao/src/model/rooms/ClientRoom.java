package model.rooms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;
import model.Book;
import protocols.ClientProtocol;
import util.BooksEngine;

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

   
    public ClientRoom(Socket client, int id, BooksEngine books) throws IOException {
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
        this.booksEngine = books;
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

            StringTokenizer token = new StringTokenizer(this.readMessage(), ClientProtocol.SEPARATOR);

            String operation = token.nextToken();
            
            switch (operation) {
                case ClientProtocol.SHOWMETHEBOOKS:
                    System.out.println("Client's looking the books");
                    LinkedList<Book> books = this.booksEngine.getBooks();
                    StringBuilder builder = new StringBuilder();

                    for (Book book : books) {
                        builder.append(book.serialize(ClientProtocol.SEPARATOR));
                    }

                    this.sendMessage(builder.toString());
                    break;
                case ClientProtocol.GIVEMETHEBOOKS:
                    System.out.println("Client's buying a book");
                    String name = token.nextToken();
                    int amount = Integer.parseInt(token.nextToken());
                    
                    Boolean result = this.booksEngine.decreaseAmount(name, amount, true);
                    this.sendMessage(result.toString());
                    break;
            }
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Erro em ClientRoom \n" + ex);
            ex.printStackTrace();
        }
    }

}
