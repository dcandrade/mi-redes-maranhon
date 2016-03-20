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
import java.util.Properties;

/**
 *
 * @author dcandrade
 */
public class LoginEngine {
    private final Properties players;
    
    public LoginEngine() throws IOException{
        this.players = new Properties();
        this.players.load(new FileInputStream("login.data"));
    }
    
    //Register a new player
    public synchronized boolean signUp(String name, String password) throws FileNotFoundException, IOException{
        if(this.players.getProperty(name) == null){
            this.players.setProperty(name, password.concat("/"+"0"));//name=password/value            
            this.players.store(new FileOutputStream("login.data"), "");
            return true; //Sucessfully registered
        }
        
        return false; //Player name already exists
    }
    
    public synchronized boolean signIn(String name, String password){
        String passwordData = this.players.getProperty(name);
     
        return passwordData.subSequence(0, passwordData.indexOf("/")).equals(password);
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
        this.players.store(new FileOutputStream("login.data"), "");
    }

}