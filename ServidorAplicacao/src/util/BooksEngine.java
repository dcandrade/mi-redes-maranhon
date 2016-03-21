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
import util.communication.MulticastCentral;

/**
 *
 * @author dcandrade
 */
public class BooksEngine {

    private final Properties players;
    private static final LinkedList<Boolean> semaphore = new LinkedList();
    private static BooksEngine instance;
    private static MulticastCentral mc;

    public static BooksEngine getInstance(MulticastCentral mc) throws IOException {
        if (BooksEngine.instance == null) {
            BooksEngine.instance = new BooksEngine();
        }
        BooksEngine.mc = mc;
        return BooksEngine.instance;
    }
    
    public static BooksEngine getInstance() throws IOException {
        if (BooksEngine.instance == null) {
            BooksEngine.instance = new BooksEngine();
        }
        return BooksEngine.instance;
    }

    private BooksEngine() throws IOException {
        this.players = new Properties();
        try {
            this.players.load(new FileInputStream("books.data"));
            Iterator it = this.players.stringPropertyNames().iterator();
            while (it.hasNext()) {
                semaphore.add(Boolean.FALSE);
                it.next();
            }
        } catch (FileNotFoundException ex) {
            FileWriter arq = new FileWriter("books.data");
            arq.close();
        }
    }

    //Register a new player
    public synchronized boolean newBook(String name, String amount, String value) throws FileNotFoundException, IOException {
        if (this.players.getProperty(name) == null) {
            this.players.setProperty(name, "");//name=amount/value            
            this.players.store(new FileOutputStream("books.data"), "");
            FileWriter file = new FileWriter(name + ".data");
            file.write(amount + "/" + value);
            file.close();
            semaphore.add(Boolean.FALSE);
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
            int amount = getAmount(name);
            double value = Double.parseDouble(getValue(name));
            Book newBook = new Book(name, amount, value);
            books.add(newBook);
        }
        return books;

    }

    public synchronized boolean turnOnSemaphore(String name) throws IOException {
        LinkedList<Book> books = getBooks();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getName().equals(name)) {
                if (!semaphore.get(i)) {
                    semaphore.set(i, Boolean.TRUE);
                    SemaphoreTimeout sto = new SemaphoreTimeout(semaphore.get(i));
                    new Thread(sto).start();//making sure semaphore will be turned off
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized void turnOffSemaphore(String name) throws IOException {
        LinkedList<Book> books = getBooks();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getName().equals(name)) {
                if (semaphore.get(i)) {
                    semaphore.set(i, Boolean.FALSE);
                    break;
                }
            }
        }
    }

    public synchronized boolean decreaseAmount(String name, int decreaseBy) throws IOException {
        //verificar se semafaro ta aceso, acender
        this.setAmount(name, "" + (this.getAmount(name) - decreaseBy));
        //apagar apos fazer a modificacao;
        
        //retornar true se foi possivel comprar, false caso contrario
        return false;
    }

    public synchronized int getAmount(String name) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(name + ".data");
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        return Integer.parseInt(line.substring(0, line.indexOf("/")));
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
