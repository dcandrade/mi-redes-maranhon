/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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

    public Purchase(String logEntry) {
        String[] split = logEntry.split(ClientProtocol.SEPARATOR);
        
        this.book = split[0];
        this.amount = Integer.parseInt(split[1]);
        this.value = Double.parseDouble(split[2]);
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

    public String serialize() {
        return this.book + ClientProtocol.SEPARATOR + this.amount
                + ClientProtocol.SEPARATOR + this.value;

    }

}
