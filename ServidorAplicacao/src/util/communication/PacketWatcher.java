/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.communication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocols.ServerProtocol;

/**
 *
 * @author dcandrade
 */
public class PacketWatcher implements Runnable {

    private static int TIMEOUT = 100;
    private MulticastCentral sender;
    private final int id;
    private int idSender;
    private final int operation;

    public PacketWatcher(int id) {
        this.operation = ServerProtocol.WAITING_CONFIRMATION;
        this.id = id;
    }

    public PacketWatcher(int id, int idSender) {
        this.operation = ServerProtocol.WAITING_RECONFIRMATION;
        this.id = id;
        this.idSender = idSender;
    }
    
    public void watch(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                if (operation == ServerProtocol.WAITING_CONFIRMATION) {
                    while (!sender.isReceived(this.id)) {
                        this.sender.resendPacket(this.id);
                        Thread.sleep(PacketWatcher.TIMEOUT);
                    }
                } else if (operation == ServerProtocol.WAITING_RECONFIRMATION) {
                    while (!sender.isTransactionFinished(id)) {
                        this.sender.sendConfirmation(id, idSender);
                        Thread.sleep(PacketWatcher.TIMEOUT);
                    }
                }
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(PacketWatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
