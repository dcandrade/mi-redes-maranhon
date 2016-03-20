/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        this.players.load(new FileInputStream("books.data"));
    }
    
    //Register a new player
    public synchronized boolean newBook(String name, String amount, String value) throws FileNotFoundException, IOException{
        if(this.players.getProperty(name) == null){
            this.players.setProperty(name, amount.concat("/"+value));//name=amount/value            
            this.players.store(new FileOutputStream("books.data"), "");
            
            return true; //Sucessfully registered
        }
        
        return false; //Player name already exists
    }
    
    public synchronized LinkedList<String[]> getBooks(){//String[0]=name 
                                                        //String[1]=amount
                                                        //String[2]=value
             Set<String> a=this.players.stringPropertyNames();
             Iterator<String> iterator = a.iterator();
             LinkedList<String[]> books = new LinkedList();
             while (iterator.hasNext()){
                 String aux = iterator.next();
                 String[] nova = new String[3];
                 nova[0]=aux;
                 nova[1]=this.getAmount(aux);
                 nova[2]=this.getValue(aux);
                 books.add(nova);
             }
             return books;

    }
    
    public synchronized String getAmount(String name){
        String amount = this.players.getProperty(name);
        amount = amount.substring(0,amount.indexOf("/"));
        return amount;
    }   
    
    public synchronized void setAmount(String name, String amount) throws FileNotFoundException, IOException{
        String aux = this.players.getProperty(name);
        aux=amount.concat(aux.substring(aux.indexOf("/"),aux.length()));
        this.players.setProperty(name, aux);            
        this.players.store(new FileOutputStream("books.data"), "");
    }
    
    public synchronized String getValue(String name){
        String value = this.players.getProperty(name);
        value = value.substring(value.indexOf("/")+1, value.length());
        return value;
    }   
    
    public synchronized void setValue(String name, String value) throws FileNotFoundException, IOException{
        String passwordData = this.players.getProperty(name);
        passwordData=passwordData.substring(0,passwordData.indexOf("/")+1).concat(value);
        this.players.setProperty(name, passwordData);            
        this.players.store(new FileOutputStream("books.data"), "");
    }

}