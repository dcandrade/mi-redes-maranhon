/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.util.LinkedList;
import model.Book;

/**
 *
 * @author Kayo
 */
public class TestBooksEngine {
    
    public static void main(String[] args) throws IOException{
        BooksEngine be = BooksEngine.getInstance();
        be.newBook("HarryPorco", "50", "150,00");
        be.newBook("HarryPorco 2", "30", "250,00");
        LinkedList<Book> a = be.getBooks();
        for (int i = 0 ; i < a.size() ; i++){
            System.out.print("Book: "+a.get(i).getName());
            System.out.print(" Amount: "+a.get(i).getAmount());
            System.out.println(" Value: "+a.get(i).getValue());
        }
        be.setAmount("HarryPorco", "100");
        be.setAmount("HarryPorco 2", "0");
        System.out.println("Amounts have changed");
        a = be.getBooks();
        for (int i = 0 ; i < a.size() ; i++){
            System.out.print("Book: "+a.get(i).getName());
            System.out.print(" Amount: "+a.get(i).getAmount());
            System.out.println(" Value: "+a.get(i).getValue());
        }
        be.setValue("HarryPorco", "200,00");
        be.setValue("HarryPorco 2", "80,00");
        System.out.println("Values have changed");
        a = be.getBooks();
        for (int i = 0 ; i < a.size() ; i++){
            System.out.print("Book: "+a.get(i).getName());
            System.out.print(" Amount: "+a.get(i).getAmount());
            System.out.println(" Value: "+a.get(i).getValue());
        }

    }
   
}
