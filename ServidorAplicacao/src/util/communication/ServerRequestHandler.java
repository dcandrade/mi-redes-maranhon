/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.communication;

import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;
import model.Book;
import model.Purchase;
import protocols.ServerProtocol;
import util.BooksEngine;
import util.LoginEngine;
import util.ShoppingLog;

/**
 *
 * @author dcandrade
 */
public class ServerRequestHandler {

    private final BooksEngine books;
    private MulticastCentral mc;
    private final ShoppingLog shopping;
    private final LoginEngine login;

    public ServerRequestHandler(BooksEngine books, ShoppingLog shopping, LoginEngine login) {
        this.books = books;
        this.shopping = shopping;
        this.login = login;
    }

    public void setMulticastCentral(MulticastCentral mc) {
        this.mc = mc;
    }

    public void processRequest(String request) throws IOException {
        StringTokenizer tokens = new StringTokenizer(request, ServerProtocol.SEPARATOR);
        String operation = tokens.nextToken();
        String book, client;
        Purchase purchase;

        switch (operation) {
            case ServerProtocol.TURN_ON_SEMAPHORE:
                book = tokens.nextToken();
                this.books.turnOnSemaphore(book);
                break;

            case ServerProtocol.TURN_OFF_SEMAPHORE:
                book = tokens.nextToken();
                this.books.turnOffSemaphore(book);
                break;

            case ServerProtocol.BUY_BOOK:
                book = tokens.nextToken();
                int amount = Integer.parseInt(tokens.nextToken());
                this.books.setAmount(book, "" + (this.books.getAmount(book) - amount));
                break;

            case ServerProtocol.NEW_CLIENT:
                client = tokens.nextToken();
                String password = tokens.nextToken();

                this.login.signUp(client, password, false);
                break;

            case ServerProtocol.NEW_PURCHASE:
                client = tokens.nextToken();

                book = tokens.nextToken();
                amount = Integer.parseInt(tokens.nextToken());
                double value = Double.parseDouble(tokens.nextToken());
                purchase = new Purchase(book, amount, value);

                this.shopping.addPurchase(client, purchase, false);
                break;

            case ServerProtocol.NEW_SERVER:
                System.out.println("[SERVER] New server, sending data");

                LinkedList<Book> bks = books.getBooks();
                StringBuilder data = new StringBuilder();

                for (Book b : bks) {
                    data.append(b.serialize(ServerProtocol.SEPARATOR));
                }

                int packet = mc.createPacket(ServerProtocol.RECEIVING_BOOKS, data.toString());
                mc.send(packet);

                packet = mc.createPacket(ServerProtocol.RECEIVING_SHOPPINGS, this.shopping.serialize());
                mc.send(packet);

                packet = mc.createPacket(ServerProtocol.RECEIVING_LOGIN, this.login.serialize());
                mc.send(packet);
                break;

            case ServerProtocol.RECEIVING_BOOKS:
                System.out.println("[SERVER] Books data received");

                while (tokens.hasMoreTokens()) {
                    String name = tokens.nextToken().replace(' ', '|');
                    String na = tokens.nextToken();
                    value = Double.parseDouble(tokens.nextToken());
                    books.newBook(name, na, String.valueOf(value));
                }

                break;

            case ServerProtocol.RECEIVING_LOGIN:
                System.out.println("[SERVER] Login data received");
                //this.login.erase();

                while (tokens.hasMoreTokens()) {
                    this.login.signUp(tokens.nextToken(), tokens.nextToken(), false);
                }
                break;

            case ServerProtocol.RECEIVING_SHOPPINGS:
                System.out.println("[SERVER] Shoppong data received");

                //this.shopping.erase();
                while (tokens.hasMoreTokens()) {
                    client = tokens.nextToken();
                    purchase = new Purchase(tokens.nextToken(),
                            Integer.parseInt(tokens.nextToken()),
                            Double.parseDouble(tokens.nextToken()));

                    this.shopping.addPurchase(client, purchase, false);
                }

                break;
        }
    }
}
