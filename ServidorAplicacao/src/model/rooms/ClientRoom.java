package model.rooms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;
import model.Book;
import model.Purchase;
import protocols.ClientProtocol;
import util.BooksEngine;
import util.LoginEngine;
import util.ShoppingLog;

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
    private final LoginEngine login;
    private final ShoppingLog shopping;
    
    public ClientRoom(Socket client, int id, BooksEngine books, ShoppingLog log, LoginEngine login) throws IOException {
        this.input = new DataInputStream(client.getInputStream());
        this.output = new DataOutputStream(client.getOutputStream());
        this.booksEngine = books;
        this.login = login;
        this.shopping = log;
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
            String user = token.nextToken();
            String password = token.nextToken();
            String operation = token.nextToken();
            
            if (operation.equals(ClientProtocol.REGISTER)) {
                boolean signUp = this.login.signUp(user, password, true);
                this.sendMessage(String.valueOf(signUp));
                return;
            } else {
                if (!this.login.signIn(user, password)) {
                    this.sendMessage(ClientProtocol.LOGIN_FAILED);
                    return;
                }
            }
            
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
                    
                    if (result) {
                        double value = Double.parseDouble(this.booksEngine.getValue(name));
                        Purchase purchase = new Purchase(name, amount, amount * value);
                        this.shopping.addPurchase(user, purchase, true);
                    }
                    
                    this.sendMessage(result.toString());
                    break;
                
                case ClientProtocol.LAST_PURCHASE:
                    Purchase lastPurchase = this.shopping.getLastPurchase(user);
                    if (lastPurchase == null) {
                        this.output.writeUTF("false");
                    } else {
                        this.output.writeUTF(lastPurchase.serialize());
                    }
                    break;
            }
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Erro em ClientRoom \n" + ex);
            ex.printStackTrace();
        }
    }
    
}
