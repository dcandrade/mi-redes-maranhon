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
import java.util.TreeMap;
import protocols.ServerProtocol;
import util.communication.MulticastCentral;

/**
 *
 * @author dcandrade
 */
public class BooksEngine {

    private final Properties players;
    private final TreeMap<String, Boolean> semaphore = new TreeMap<>();
    private MulticastCentral mc;

    public BooksEngine() throws IOException {
        this.players = new Properties();
        try {
            this.players.load(new FileInputStream("books.data"));
            Iterator<String> it = this.players.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String name = it.next();
                this.semaphore.put(name, Boolean.FALSE);
            }
        } catch (FileNotFoundException ex) {
            FileWriter arq = new FileWriter("books.data");
            arq.close();
        }
    }

    public void setMulticastCentral(MulticastCentral mc) {
        this.mc = mc;
    }

    //Register a new player
    public synchronized boolean newBook(String name, String amount, String value) throws FileNotFoundException, IOException {
        if (this.players.getProperty(name) == null) {
            this.players.setProperty(name, "");//name=amount/value            
            this.players.store(new FileOutputStream("books.data"), "");
            FileWriter file = new FileWriter(name + ".data");
            file.write(amount + "/" + value);
            file.close();
            semaphore.put(name, Boolean.FALSE);
            return true; //Sucessfully registered
        }

        return false; //Player name already exists
    }

    public synchronized LinkedList<Book> getBooks() throws IOException {
        Set<String> a = this.players.stringPropertyNames();
        Iterator<String> iterator = a.iterator();
        LinkedList<Book> books = new LinkedList();
        while (iterator.hasNext()) {
            String name = iterator.next();
            int amount = getAmount(name, true);
            double value = Double.parseDouble(getValue(name));
            Book newBook = new Book(name, amount, value);
            books.add(newBook);
        }
        return books;

    }

    public synchronized boolean turnOnSemaphore(String name) throws IOException {
        Boolean get = this.semaphore.get(name);

        if (get != null && !get) {
            this.semaphore.put(name, Boolean.TRUE);

            SemaphoreTimeout sto = new SemaphoreTimeout(this.semaphore.get(name));
            new Thread(sto).start();//making sure semaphore will be turned off
            return true;
        }

        return false;
    }

    public synchronized boolean turnOffSemaphore(String name) throws IOException {
        Boolean get = this.semaphore.get(name);

        if (get != null && get) {
            this.semaphore.put(name, Boolean.FALSE);
            return true;
        }

        return false;
    }

    private boolean getSemaphoreStatus(String name) {
        Boolean status = this.semaphore.get(name);

        return status == null ? false : status;
    }

    @SuppressWarnings("empty-statement")
    public synchronized boolean decreaseAmount(String name, int decreaseBy, boolean propagate) throws IOException {
        //verificar se semafaro ta aceso, acender
        while (!this.turnOnSemaphore(name));
        if (propagate) {
            //turning sempaphore on, it'll be on looping untill semaphore's true
            int packet = this.mc.createPacket(ServerProtocol.TURN_ON_SEMAPHORE, name);
            this.mc.send(packet);
        }
        System.out.println("[BOOKS] " + name + "'s Semaphore is On");
        //send packet to change the semaphore in all servers
        //now all semaphores are true, it's possible to write on file
        if ((this.getAmount(name, true) - decreaseBy) < 0) {//there aren't books enough
            if (propagate) {
                int packet = this.mc.createPacket(ServerProtocol.TURN_OFF_SEMAPHORE, name);
                this.mc.send(packet);
                this.turnOffSemaphore(name);
            }
            System.out.println("[BOOKS] There aren't books enough, semaphore is off again");
            return false;
        }
        this.setAmount(name, "" + (this.getAmount(name, true) - decreaseBy));
        //  int value = this.getAmount(name) - decreaseBy;
        if (propagate) {
            int packet = this.mc.createPacket(ServerProtocol.BUY_BOOK, name + ServerProtocol.SEPARATOR + decreaseBy);
            this.mc.send(packet);

            //send packet to change amount in all servers, now everyone has the same copy
            packet = this.mc.createPacket(ServerProtocol.TURN_OFF_SEMAPHORE, name);

            //now semaphores are false again, anyone is abble to write
            this.mc.send(packet);
        }
        this.turnOffSemaphore(name);

        System.out.println("[BOOKS]Changes were propageted");
        System.out.println("[BOOKS]" + name + "'s semaphore is now off");
        return true;//file updated and book was bought
    }

    @SuppressWarnings("empty-statement")
    private synchronized int getAmount(String name, boolean inside) throws FileNotFoundException, IOException {
   
        
        FileReader fr = new FileReader(name + ".data");
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        

        
        return Integer.parseInt(line.substring(0, line.indexOf("/")));
    }

    public synchronized int getAmount(String name) throws IOException {
        return this.getAmount(name, false);
    }

    public synchronized void setAmount(String name, String amount) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(name + ".data");
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        String newLine;
        newLine = amount.concat(line.substring(line.indexOf("/"), line.length()));
        fr.close();
        FileWriter fw = new FileWriter(name + ".data");
        fw.write(newLine);
        fw.close();
    }

    public synchronized String getValue(String name) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(name + ".data");
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        return line.substring(line.indexOf("/") + 1, line.length());
    }

    public synchronized void setValue(String name, String value) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(name + ".data");
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        String newLine;
        newLine = line.substring(0, line.indexOf("/") + 1) + value;
        fr.close();
        FileWriter fw = new FileWriter(name + ".data");
        fw.write(newLine);
        fw.close();
    }

}
