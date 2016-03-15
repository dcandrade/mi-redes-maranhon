/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import model.listeners.ClientListener;
import model.rooms.Heartbeat;
import protocols.CentralizerProtocol;

/**
 *
 * @author dcandrade
 */
public class ApplicationServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Conectando com o centralizador...");
        Socket socket = new Socket(CentralizerProtocol.IP, CentralizerProtocol.PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        dos.writeInt(CentralizerProtocol.IM_A_SERVER);
        int id = dis.readInt();
        int port = dis.readInt();
        System.out.println("ID Adquirida: " + id);
        ClientListener clientListener = new ClientListener(id, port);
        Heartbeat heartbeat = new Heartbeat(dos);
        heartbeat.beat();
        clientListener.run();
    }
}
