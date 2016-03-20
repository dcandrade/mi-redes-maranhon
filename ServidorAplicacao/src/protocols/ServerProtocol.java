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
    public static final int BEAT = 400;
    public final static int PORT = 12345;
    public final static int ITS_A_SERVER = 2;
    public final static String SEPARATOR = "-";
    public final static int SERVER_STUFF = 0250;
    public final static int MULTICAST_STUFF = 0520;
    public final static int CONFIRMATION = 01; //ID PACKET + ID SENDER +  + CONFIRMATION
    public final static int RECONFIRMATION = 02; //ID PACKET + ID SENDER
    public final static int WAITING_CONFIRMATION = 54365;
    public final static int WAITING_RECONFIRMATION = 3235;
    public final static String RECEIVED = "ITSWITHME";
}