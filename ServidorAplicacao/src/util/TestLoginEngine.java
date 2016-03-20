/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;

/**
 *
 * @author Kayo
 */
public class TestLoginEngine {
        
    public static void main (String[] args) throws IOException{
        LoginEngine a = new LoginEngine();
        a.signUp("Kayo", "123");
        boolean verifica = a.signIn("Kayo", "123");
        a.setValue("Kayo", "150");
        if (verifica)
            System.out.println("Value"+ a.getValue("Kayo"));
    }
}
