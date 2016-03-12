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

/**
 *
 * @author Kayo
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Socket novo = new Socket("localhost",12345);
        
        DataOutputStream saida = new DataOutputStream(novo.getOutputStream());
        DataInputStream entrada = new DataInputStream(novo.getInputStream());
        saida.writeInt(1);
        String endereco = entrada.readUTF();
        String ip = endereco.substring(0,endereco.indexOf("/"));
        String porta = endereco.substring(endereco.indexOf("/")+1,endereco.length());
        novo = new Socket (ip, Integer.parseInt(porta));

        while (true){
            System.out.println("Aqui");
            
        }
    }
    
}
