/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Kayo
 */
public class TestBooksEngine {
    
    public static void main(String[] args) throws IOException{
        BooksEngine be = new BooksEngine();
        be.newBook("Harry Porco", "50", "150,00");
        be.newBook("Harry Porco 2", "30", "250,00");
        LinkedList<String[]> a = be.getBooks();
        for (int i = 0 ; i < a.size() ; i++){
            System.out.print("Book: "+a.get(i)[0]);
            System.out.print(" Amount: "+a.get(i)[1]);
            System.out.println(" Value: "+a.get(i)[2]);
        }
        be.setAmount("Harry Porco", "100");
        be.setAmount("Harry Porco 2", "0");
        System.out.println("Amounts have changed");
        a = be.getBooks();
        for (int i = 0 ; i < a.size() ; i++){
            System.out.print("Book: "+a.get(i)[0]);
            System.out.print(" Amount: "+a.get(i)[1]);
            System.out.println(" Value: "+a.get(i)[2]);
        }
        be.setValue("Harry Porco", "200,00");
        be.setValue("Harry Porco 2", "80,00");
        System.out.println("Values have changed");
        a = be.getBooks();
        for (int i = 0 ; i < a.size() ; i++){
            System.out.print("Book: "+a.get(i)[0]);
            System.out.print(" Amount: "+a.get(i)[1]);
            System.out.println(" Value: "+a.get(i)[2]);
        }
        
    }
   
}
