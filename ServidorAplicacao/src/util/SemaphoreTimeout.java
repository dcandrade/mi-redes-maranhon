/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Kayo
 */
public class SemaphoreTimeout implements Runnable{
    private static final int TIMEOUT = 500;
    private Boolean semaphore;
    public SemaphoreTimeout (Boolean semaphore){
        this.semaphore=semaphore;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException ex) {
            Logger.getLogger(SemaphoreTimeout.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(semaphore)//if it's still on, server should've been disconected 
            semaphore=Boolean.FALSE;

    }
    
}
