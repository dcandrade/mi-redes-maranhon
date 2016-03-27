/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import model.Purchase;
import protocols.ClientProtocol;
import protocols.ServerProtocol;
import util.communication.MulticastCentral;

/**
 *
 * @author dcandrade
 */
public class ShoppingLog {

    private final Properties log;
    private final File file;
    private MulticastCentral mc;

    public ShoppingLog() throws FileNotFoundException, IOException {
        this.log = new Properties();
        File file= new File("files/books");
        file.mkdirs();
        this.file = new File("files/ShoppingLog.data");

        try {
            this.log.load(new FileInputStream(this.file));
        } catch (FileNotFoundException ex) {
            FileWriter fw = new FileWriter(this.file);
            fw.close();
            this.log.load(new FileInputStream(this.file));
        }
    }

    public void setMulticastCentral(MulticastCentral mc) {
        this.mc = mc;
    }

    public void erase() {
        this.file.delete();
    }

    public void addPurchase(String client, Purchase purchase, boolean propagate) throws IOException {
        this.log.put(client, purchase.serialize());
        this.log.store(new FileOutputStream(this.file), "");

        if (propagate) {
            int packet = this.mc.createPacket(ServerProtocol.NEW_PURCHASE, client + purchase.serialize());
            this.mc.send(packet);
        }
    }

    public String serialize() {
        Set<Map.Entry<Object, Object>> entries = this.log.entrySet();

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Object, Object> entry : entries) {
            result.append(entry.getKey().toString()).append(ClientProtocol.SEPARATOR);
            result.append(entry.getValue().toString()).append(ClientProtocol.SEPARATOR);
        }

        return result.toString();
    }

    public Purchase getLastPurchase(String client) {
        String last = this.log.getProperty(client);
        
        return last == null? null : Purchase.unserialize(last);
    }

}
