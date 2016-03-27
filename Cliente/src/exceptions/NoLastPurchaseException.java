/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author dcandrade
 */
public class NoLastPurchaseException extends Exception{

    public NoLastPurchaseException() {
        super("This user have no purchases");
    }
    
}
