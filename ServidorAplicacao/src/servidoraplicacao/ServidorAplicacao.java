/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidoraplicacao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Kayo
 */
public class ServidorAplicacao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Socket novo = new Socket("localhost",12345);
        
        DataOutputStream saida = new DataOutputStream(novo.getOutputStream());
        DataInputStream entrada = new DataInputStream(novo.getInputStream());
            saida.writeInt(0);
            int porta = entrada.readInt();
        while (true){
           
            System.out.println("Aqui");

            ServerSocket servidor = new ServerSocket(porta);
                        System.out.println("Aqui");

            System.out.println("Porta "+porta+ "aberta!");
              // aceita um cliente
            Socket cliente = servidor.accept();
                        System.out.println("Aqui");

            System.out.println("Nova conexão com o cliente "
                    + cliente.getInetAddress().getHostAddress()
            );
            
        }
    }
    
}
