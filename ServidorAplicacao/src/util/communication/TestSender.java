/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.communication;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author dcandrade
 */
public class TestSender {

    public static void main(String[] args) throws UnknownHostException, IOException {
        MulticastCentral mc = new MulticastCentral(5, null, true);
        MulticastReceiver mr = new MulticastReceiver(mc);
        mr.start();
        int packetID = mc.createPacket(9, "test packet");
        mc.send(packetID);
    }
}
