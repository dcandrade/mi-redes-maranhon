/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.util.StringTokenizer;

/**
 *
 * @author dcandrade
 */
public class Book {

    private final String name;
    private final int amount;
    private final double value;
    private int id;

    public Book(String name, int amount, double value) {
        this.name = name;
        this.amount = amount;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public Double getValue() {
        return this.value;
    }

    public String serialize(String separator) {
        return this.name + separator + this.amount + separator + this.value + separator;
    }

    @Override
    public String toString() {
        return this.name + ", amount: " + this.amount + ", price: " + this.value;
    }
}
