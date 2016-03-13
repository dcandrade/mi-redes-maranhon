/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dcandrade
 */
public class ServerWatcher extends Thread{
    private ServidorAplicacao server;
    public static final int TIMEOUT = 5000;
    public static final int MAX_TOLERANCE = 1000;
    private Controller controller;
    private Counter counter;
    
    public ServerWatcher(ServidorAplicacao server, Controller controller) {
        this.server = server;
        this.controller = controller;
    }
    
    private void killServer(){
        this.controller.removerServidor(server.getIp());
    }
    
    public void timeIsOver(){
        this.killServer();
        this.interrupt();
    }

    @Override
    public void run() {
        this.counter = new Counter(this, TIMEOUT + MAX_TOLERANCE);
        while(true){
            try {
                this.server.receberDado();
                this.counter.interrupt();
                this.counter = new Counter(this, TIMEOUT+MAX_TOLERANCE);
                this.counter.start();
            } catch (IOException ex) {
            }
        }
    }
    
    
    
    
}
