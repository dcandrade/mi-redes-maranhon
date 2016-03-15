/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.rooms;

import java.io.DataOutputStream;
import protocols.CentralizerProtocol;

/**
 *
 * @author dcandrade
 */
public class Heartbeat implements Runnable {

    private static final int TIMEOUT = 500;
    private final DataOutputStream dos;

    public Heartbeat(DataOutputStream dos) {
        this.dos = dos;
    }
    
    public void beat(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.dos.writeUTF(CentralizerProtocol.BEAT);
                Thread.sleep(TIMEOUT);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
