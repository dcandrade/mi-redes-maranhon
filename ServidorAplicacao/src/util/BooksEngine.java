/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import model.Book;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;


/**
 *
 * @author dcandrade
 */
public class BooksEngine {
    private final Properties players;
    
    public BooksEngine() throws IOException{
        this.players = new Properties();
        try {
            this.players.load(new FileInputStream("books.data"));
        } catch (FileNotFoundException ex) {
            FileWriter arq = new FileWriter("books.data");
            arq.close();

        }
    }
    
    //Register a new player
    public synchronized boolean newBook(String name, String amount, String value) throws FileNotFoundException, IOException{
        if(this.players.getProperty(name) == null){
            this.players.setProperty(name,"");//name=amount/value            
            this.players.store(new FileOutputStream("books.data"), "");
            FileWriter file = new FileWriter(name+".data");
            file.write(amount+"/"+value);
            file.close();

            return true; //Sucessfully registered
        }
        
        return false; //Player name already exists
    }
    
    public synchronized LinkedList<Book> getBooks() throws IOException{
             Set<String> a=this.players.stringPropertyNames();
             Iterator<String> iterator = a.iterator();
             LinkedList<Book> books = new LinkedList();
             while (iterator.hasNext()){
                 String name = iterator.next();
                 String amount = getAmount(name);
                 String value = getValue(name);
                 Book newBook = new Book(name,amount,value);
                 books.add(newBook);
             }
             return books;

    }
    
    public synchronized String getAmount(String name) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(name+".data");
        BufferedReader reader = new BufferedReader(fr);
        String line=reader.readLine();
        return line.substring(0,line.indexOf("/"));
    }   
    
    public synchronized void setAmount(String name, String amount) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(name+".data");
        BufferedReader reader = new BufferedReader(fr);
        String line=reader.readLine();
        String newLine;
        newLine= amount.concat(line.substring(line.indexOf("/"),line.length()));
        fr.close();
        FileWriter fw = new FileWriter(name+".data");
        fw.write(newLine);
        fw.close();
    }
    
    public synchronized String getValue(String name) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(name+".data");
        BufferedReader reader = new BufferedReader(fr);
        String line=reader.readLine();
        return line.substring(line.indexOf("/")+1, line.length());
    }   
    
    public synchronized void setValue(String name, String value) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(name+".data");
        BufferedReader reader = new BufferedReader(fr);
        String line=reader.readLine();
        String newLine;
        newLine= line.substring(0,line.indexOf("/")+1)+value;
        fr.close();
        FileWriter fw = new FileWriter(name+".data");
        fw.write(newLine);
        fw.close();
    }

}