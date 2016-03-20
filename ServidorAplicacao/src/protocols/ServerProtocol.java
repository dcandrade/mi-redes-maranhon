/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocols;

/**
 *
 * @author dcandrade
 */
public class ServerProtocol {

    public static final int BEAT = 0;
    public final static int ITS_A_SERVER = 1;
    public final static int SERVER_STUFF = 2;
    public final static int MULTICAST_STUFF = 3;
    public final static int CONFIRMATION = 4; //ID PACKET + ID SENDER +  + CONFIRMATION
    public final static int RECONFIRMATION = 5; //ID PACKET + ID SENDER
    public final static int WAITING_CONFIRMATION = 6;
    public final static int WAITING_RECONFIRMATION = 7;

    public final static String RECEIVED = "ITSWITHME";
    public final static String MULTICAST_ADDRESS = "239.0.0.0";
    public final static int PORT = 12345;
    public final static String SEPARATOR = "-";
}
