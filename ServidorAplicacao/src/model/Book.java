/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.StringTokenizer;

/**
 *
 * @author Kayo
 */
public class Book {
      private final String name;
    private final int amount;
    private final double value;

    public Book(String name, int amount, double value) {
        this.name = name;
        this.amount = amount;
        this.value = value;
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
   
    
    public String serialize(String separator){
        return this.name + separator + this.amount + separator + this.value + separator;
    }
    
    @Override
    public String toString() {
        return "Livro: "+this.name+", quantidade: "+this.amount + ", preço: "+this.value;
    }
    
    
}
