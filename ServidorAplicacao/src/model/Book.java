/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
/**
 *
 * @author Kayo
 */
public class Book {
    private final String name;
    private final String amount;
    private final String value;
    
    public Book (String name, String amount, String value){
        this.name = name;
        this.amount = amount;
        this.value = value;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getAmount(){
        return this.amount;
    }
    
    public String getValue(){
        return this.value;
    }
    
}
