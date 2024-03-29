/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.StringTokenizer;
import protocols.ClientProtocol;

/**
 *
 * @author dcandrade
 */
public class Purchase {

    private final String book;
    private final int amount;
    private final double value;

    public Purchase(String book, int amount, double value) {
        this.book = book;
        this.amount = amount;
        this.value = value;
    }

    private Purchase(String logEntry) {
        StringTokenizer tokens = new StringTokenizer(logEntry, ClientProtocol.SEPARATOR);
        
        this.book = tokens.nextToken();
        this.amount = Integer.parseInt(tokens.nextToken());
        this.value = Double.parseDouble(tokens.nextToken());
    }

    public String getBook() {
        return book;
    }

    public int getAmount() {
        return amount;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.serialize();
    }
    

    public String serialize() {
        return ClientProtocol.SEPARATOR + this.book + ClientProtocol.SEPARATOR + this.amount
                + ClientProtocol.SEPARATOR + this.value + ClientProtocol.SEPARATOR;

    }
    
    public static Purchase unserialize(String purchase){
        return new Purchase(purchase);
    }

}
