/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import protocols.Protocol;

/**
 *
 * @author Kayo
 * @author Daniel Andrade
 */
public class Client {

    DataInputStream input;
    DataOutputStream output;

    /**
     * 
     * @return A string array. [0] = IP, [1] = Port
     * @throws IOException 
     */
    public String[] askForServer() throws IOException {
        System.out.println("Solicitando IP ao Redirecionador....");
        Socket socket = new Socket(Protocol.SERVER, Protocol.PORT);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        out.writeInt(Protocol.IM_A_CLIENT);
        String response = in.readUTF();
        System.out.println(response);
        String[] data = response.split(Protocol.SEPARATOR);
        System.out.println("Recebido: "+data[0]+ ":"+data[1]);
        return data;
    }

    public void connectToServer(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Conectado ao servidor.");
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        
        String[] data = client.askForServer();
        client.connectToServer(data[0], Integer.parseInt(data[1]));

        while (true);

    }

}
