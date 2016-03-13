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
        Socket novo = new Socket("192.168.25.225",12345);
        
        DataOutputStream saida = new DataOutputStream(novo.getOutputStream());
        DataInputStream entrada = new DataInputStream(novo.getInputStream());
        saida.writeInt(1);
        String endereco = entrada.readUTF();
        String ip = endereco.substring(0,endereco.indexOf("/"));
        int porta = Integer.parseInt(endereco.substring(endereco.indexOf("/")+1,endereco.length()));
        
        System.out.println("IP: "+ip+"Porta: "+porta+"");
        novo.close();
        
        novo = new Socket (ip, porta);
        saida = new DataOutputStream(novo.getOutputStream());

        System.out.println("IP: "+ip+"Porta: "+porta+"");
        while (true){
            System.out.println("Aqui");
            
        }
    }
    
}
