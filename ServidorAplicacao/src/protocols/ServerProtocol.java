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
    public final static String ITS_A_SERVER = "Hey, i'm a server";
    public final static String SERVER_STUFF = "Application data";
    public final static String MULTICAST_STUFF = "Communication data";
    public final static String CONFIRMATION = "Packet was received, bro"; //ID PACKET + ID SENDER +  + CONFIRMATION
    public final static String RECONFIRMATION = "Now i know"; //ID PACKET + ID SENDER
    public final static String WAITING_CONFIRMATION = "Did you received the packet?";
    public final static String WAITING_RECONFIRMATION = "Did you now that i received the packet?";
    public final static String TURN_ON_SEMAPHORE = "Turn it on"; // NOME DO LIVRO
    public final static String TURN_OFF_SEMAPHORE = "Turn it off, i'm done";// NOME DO LIVRO
    public final static String BUY_BOOK = "Buy book"; //NOME LIVRO + QUANTIDADE
    public final static String RECEIVED = "I have it";
    public final static String NEW_SERVER = "New Server";
    public final static String RECEIVING_BOOKS = "Receiving Books";
    public final static String MULTICAST_ADDRESS = "192.168.25.0";
    public final static int PORT = 12345;
    public final static String SEPARATOR = "-";
}
