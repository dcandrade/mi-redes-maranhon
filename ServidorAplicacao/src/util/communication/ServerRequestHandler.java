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
import protocols.ServerProtocol;
import util.BooksEngine;

/**
 *
 * @author dcandrade
 */
public class ServerRequestHandler {

    private final BooksEngine books;
    private MulticastCentral mc=null;
    public ServerRequestHandler(BooksEngine books) throws IOException {
        this.books = books;
    }
    public void setMulticastCentral(MulticastCentral mc){
        this.mc=mc;
    }

    public void processRequest(String request) throws IOException {
        StringTokenizer token = new StringTokenizer(request, ServerProtocol.SEPARATOR);
        String operation = token.nextToken();
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
                this.books.setAmount(book, ""+(this.books.getAmount(book)-amount));
                break;
            case ServerProtocol.NEW_SERVER:
                System.out.println("[SERVER] New server on, requesting data");
                LinkedList<Book> bks = books.getBooks();
                book = "";
                for (int i = 0; i<bks.size(); i++){
                    Book nb = bks.get(i);
                    book=book+nb.serialize("-")+"-";
                    System.out.println("Book: "+book);
                }
                System.out.println("Book: "+book);

                int packet=mc.createPacket(ServerProtocol.RECEIVING_BOOKS, book);
                mc.send(packet);
                //criar pacote de log
                
                break;
            case ServerProtocol.RECEIVING_BOOKS:
                System.out.println("[SERVER] New server on, receiveing books");
                while(token.hasMoreElements()){
                String name=token.nextToken();
                String na = token.nextToken();
                String value = token.nextToken();
                System.out.println("name"+name + "value"+value+"amount"+na);
                books.newBook(name, na, value);
                }
                break;
                //pegar cada "-", quebrar a string formada em / e chamar book.newBook(dado1,dado2,dado3)
        }
    }
}
