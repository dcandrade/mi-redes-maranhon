/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import model.Controller;
import model.ServerRoom;
import protocolos.ClientProtocol;
import protocolos.ServerProtocol;
import util.IPTuple;

/**
 *
 * @author Kayo
 */
public class Centralizer {

    private static int ServerID = 0;
    private static int PORT = 8801;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(ClientProtocol.PORTA);
        System.out.println("Porta " + ClientProtocol.PORTA + " aberta.");
        System.out.println("Servidor Online.");

        Controller controller = new Controller();

        while (true) {
            // aceita um cliente
            
            Socket client = server.accept();
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            DataInputStream input = new DataInputStream(client.getInputStream());

            String kindOfClient = input.readUTF();

            int port;
            switch (kindOfClient) {
                case ClientProtocol.IM_A_CLIENT:
                    System.out.println("Novo cliente:  " + client.getInetAddress().getHostAddress());
                    IPTuple applicationServer = controller.getProximoServidor();
                    StringBuilder packet = new StringBuilder();
                    //packet.append(ClientProtocol.IP_SERVIDOR); //Operation
                    //packet.append(ClientProtocol.SEPARADOR);
                    packet.append(applicationServer.getIP()); //Server's IP
                    packet.append(ClientProtocol.SEPARADOR);
                    packet.append(applicationServer.getPort()); //Server's Port
                    output.writeUTF(packet.toString());//Sends the address to the new client.
                    client.close(); //Closes the conection
                    break;
                case ServerProtocol.ITS_A_SERVER:
                    port = Centralizer.PORT++;
                    System.out.println("Servidor se preparando: " + client.getInetAddress().getHostAddress() + " : " + port);
                    output.writeInt(port);
                    output.writeInt(ServerID++);
                    
                    ServerRoom sr = new ServerRoom(controller, input, output, client.getInetAddress().getHostAddress());
                    Thread t = new Thread(sr);
                    t.start();
                    break;
            }
        }
    }
}
