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

            switch (kindOfClient) {
                case ClientProtocol.IM_A_CLIENT:
                    System.out.println("Novo cliente:  " + client.getInetAddress().getHostAddress());
                    if(controller.amountOfServers()!=0){
                    output.writeBoolean(true);
                    System.out.println("true");
                    IPTuple applicationServer = controller.getProximoServidor();
                    StringBuilder packet = new StringBuilder();
                    //packet.append(ClientProtocol.IP_SERVIDOR); //Operation
                    //packet.append(ClientProtocol.SEPARADOR);
                    packet.append(applicationServer.getIP()); //Server's IP
                    packet.append(ClientProtocol.SEPARADOR);
                    packet.append(applicationServer.getPort()); //Server's Port
                    output.writeUTF(packet.toString());//Sends the address to the new client.
                    }
                    else{
                        output.writeBoolean(false);
                        System.out.println("false");

                    }
                    client.close(); //Closes the conection
                    break;
                case ServerProtocol.ITS_A_SERVER:
                    int port = Centralizer.PORT++;
                    System.out.println("Servidor conectado: " +client.getInetAddress().getHostAddress() + " : " + port);
                    output.writeInt(port);
                    output.writeInt(ServerID++);
                    String ip = client.getInetAddress().getHostAddress();
                    controller.addServer(ip, port, input, output);
                    System.out.println("Total de Servidores: " + controller.amountOfServers());
                    break;
            }
        }
    }
}
