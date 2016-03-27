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
import protocols.ClientProtocol;
import protocols.ServerProtocol;
import util.communication.MulticastCentral;

/**
 *
 * @author dcandrade
 */
public class LoginEngine {

    private final Properties log;
    private final File file;
    private MulticastCentral mc;

    public LoginEngine() throws IOException {
        this.log = new Properties();
        File file= new File("files/books");
        file.mkdirs();
        this.file = new File("files/login.data");

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

    //Register a new player
    public synchronized boolean signUp(String name, String password, boolean propagate) throws FileNotFoundException, IOException {
        if (this.log.getProperty(name) == null) {
            this.log.setProperty(name, password);
            this.log.store(new FileOutputStream(this.file), "Added a new Client");

            if (propagate) {
                int packet = this.mc.createPacket(ServerProtocol.NEW_CLIENT, name + ServerProtocol.SEPARATOR + password);
                this.mc.send(packet);
            }

            return true; //Sucessfully registered
        }

        return false; //Player name already exists
    }

    public synchronized boolean signIn(String name, String password) {
        String passwordData = this.log.getProperty(name);

        return password.equals(passwordData);
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

}
