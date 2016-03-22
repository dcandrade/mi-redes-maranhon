/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.communication;

import java.io.IOException;
import java.util.StringTokenizer;
import protocols.ServerProtocol;
import util.BooksEngine;

/**
 *
 * @author dcandrade
 */
public class ServerRequestHandler {

    private final BooksEngine books;

    public ServerRequestHandler() throws IOException {
        this.books = BooksEngine.getInstance();
    }

    public void processRequest(String request) throws IOException {
        StringTokenizer token = new StringTokenizer(request, ServerProtocol.SEPARATOR);
        int operation = Integer.parseInt(token.nextToken());

        String book;

        switch (operation) {
            case ServerProtocol.TURN_ON_SEMAPHORE:
                //compra do livroBUY_BOOK
                book = token.nextToken();
                this.books.turnOnSemaphore(book);
                break;
            case ServerProtocol.TURN_OFF_SEMAPHORE:
                book = token.nextToken();
                this.books.turnOffSemaphore(book);
                break;
            case ServerProtocol.BUY_BOOK:
                book = token.nextToken();
                int amount = Integer.parseInt(token.nextToken());
                System.err.println("Compra de Livro " + book+":"+amount);
                this.books.decreaseAmount(book, amount, false);
                break;
        }
    }
}
