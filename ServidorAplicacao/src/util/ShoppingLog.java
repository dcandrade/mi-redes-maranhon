/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import protocols.ClientProtocol;

/**
 *
 * @author dcandrade
 */
public class ShoppingLog {
    private static final String DIR = "/logs";
    private final Properties log;
    private final String client;
    
    public ShoppingLog(String client) throws IOException{
        this.client = client;
        
        File dir = new File(ShoppingLog.DIR);
        dir.mkdir();
        
        this.log = new Properties();
        this.log.load(new FileInputStream(this.client));
    }
    
    public void addPurchase(String book, int amount) throws IOException{
        String value = book + ClientProtocol.SEPARATOR + amount;
        String key = String.valueOf(System.currentTimeMillis());
        
        this.log.put(key, value);
        this.store();
        
    }
    
    public SortedSet<String> getRecentPurchases(int amount){
        Enumeration<?> propertyNames = this.log.propertyNames();
        
        SortedSet<String> set = new TreeSet<>();
        
        while(propertyNames.hasMoreElements()){
            set.add(String.valueOf(set));
        }
        
        Iterator<String> it = set.iterator();
        String last = null;
        for(int i=0; it.hasNext() && i<amount; i++){
            last = it.next();
        }
        
        return set.subSet(set.first(), last);
    }
    
    private void store() throws IOException{
        this.log.store(new FileOutputStream(client), "");
    }
    
}
