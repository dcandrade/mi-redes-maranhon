/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;

/**
 *
 * @author dcandrade
 */
public class ServerWatcher extends Thread{
    private final ApplicationServer server;
    public static final int TIMEOUT = 500;
    public static final int MAX_TOLERANCE = 100;
    private final Controller controller;
    private  boolean isAlive = true;
    
    public ServerWatcher(ApplicationServer server, Controller controller) {
        this.server = server;
        this.controller = controller;
    }
    
    private void killServer(){
        this.controller.removeServer(server.getIpPort());
        System.out.println("Um servidor ficou offline!");
    }
    
    public void timeIsOver(){
        this.killServer();
        this.interrupt();
    }

    @Override
    public void run() {
        while(this.isAlive){
            try {
                this.server.recieveData();
            } catch (IOException ex) {
                //Servidor caiu
                this.killServer();
                this.isAlive = false;
            }
        }
    }
    
    
    
    
}
