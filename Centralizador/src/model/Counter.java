/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dcandrade
 */
public class Counter extends Thread{
    private final int time;
    private final ServerWatcher watcher;

    public Counter(ServerWatcher watcher, int time) {
        this.time = time;
        this.watcher = watcher;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(this.time);
            this.watcher.timeIsOver();
        } catch (InterruptedException ex) {
            Logger.getLogger(Counter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
