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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import model.Purchase;
import protocols.ClientProtocol;

/**
 *
 * @author dcandrade
 */
public class ShoppingLog {

    private static final String DIR = "/logs";
    private final Properties log;
    private final String client;
    private final String clientFile;

    public ShoppingLog(String client) throws IOException {
        this.client = client;
        this.clientFile = ShoppingLog.DIR + "/" + client + ".sLog";

        File dir = new File(ShoppingLog.DIR);
        dir.mkdir();

        this.log = new Properties();
        this.log.load(new FileInputStream(this.clientFile));
    }

    public void addPurchase(String book, int amount, double total) throws IOException {
        String value = book + ClientProtocol.SEPARATOR
                + amount + ClientProtocol.SEPARATOR + total;
        String key = String.valueOf(System.currentTimeMillis());

        this.log.put(key, value);
        this.store();
    }

    public List<Purchase> getRecentPurchases(int amount) {
        Enumeration<?> propertyNames = this.log.propertyNames();

        Comparator<Long> reverse = new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }

        };

        SortedSet<Long> set = new TreeSet<>(reverse);

        while (propertyNames.hasMoreElements()) {
            set.add(Long.parseLong(propertyNames.nextElement().toString()));
        }

        Iterator<Long> it = set.iterator();

        List<Purchase> result = new ArrayList<>();
        
        for(int i = 0; i<amount || it.hasNext(); i++){
            String logEntry = this.log.getProperty(String.valueOf(it.next()));
            
            result.add(new Purchase(logEntry));
        }
        
        return result;
    }

    private void store() throws IOException {
        this.log.store(new FileOutputStream(client), "");
    }

}
