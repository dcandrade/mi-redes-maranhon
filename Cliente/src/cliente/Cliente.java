/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
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
public class Cliente {

    public static void main(String[] args) throws IOException {
        System.out.println("Conectando ao redirecionador");
        Socket socket = new Socket(Protocol.SERVER, Protocol.PORT);

        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
        DataInputStream entrada = new DataInputStream(socket.getInputStream());
        saida.writeInt(Protocol.IM_A_CLIENT);

        StringTokenizer endereco = new StringTokenizer(entrada.readUTF(), Protocol.SEPARATOR);
        System.out.println("Endereço do servidor adquirido");
        socket.close();
        
        endereco.nextToken();
        String ip = endereco.nextToken();
        int port = Integer.parseInt(endereco.nextToken());
        
        socket = new Socket(ip, port);
        
        System.out.println("Conectado!");
        
        while(true);
        
    }

}
