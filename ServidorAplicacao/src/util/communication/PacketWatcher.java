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

    private static int TIMEOUT = 2000;
    private static int ATTEMPTS = 3;
    private MulticastCentral sender;
    private final int id;
    private int idSender;
    private final String operation;

    public PacketWatcher(int packetID, MulticastCentral sender) {
        this.operation = ServerProtocol.WAITING_CONFIRMATION;
        this.id = packetID;
        this.sender = sender;
    }

    public PacketWatcher(int id, int idSender, MulticastCentral sender) {
        this.operation = ServerProtocol.WAITING_RECONFIRMATION;
        this.id = id;
        this.idSender = idSender;
        this.sender = sender;
    }

    public void watch() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                 int attempts;
                switch (operation) {
                    case ServerProtocol.WAITING_CONFIRMATION:
                        attempts=1;
                        this.wait(PacketWatcher.TIMEOUT);
                        while (!sender.isReceived(this.id)&& attempts < PacketWatcher.ATTEMPTS) {
                            System.out.println("Resending packet, attempt #"+attempts);
                            this.sender.resendPacket(this.id);
                            attempts++;
                            this.wait(PacketWatcher.TIMEOUT);
                        }
                    case ServerProtocol.WAITING_RECONFIRMATION:
                        attempts=1;
                        this.wait(PacketWatcher.TIMEOUT);
                        while (!sender.isTransactionFinished(id) && attempts < PacketWatcher.ATTEMPTS) {
                             System.out.println("Resending confirmation, attempt #"+attempts);
                            this.sender.sendConfirmation(id, idSender);
                            attempts++;
                            this.wait(PacketWatcher.TIMEOUT);
                        }
                        break;
                }
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(PacketWatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
