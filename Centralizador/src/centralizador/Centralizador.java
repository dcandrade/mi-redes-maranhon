/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizador;

import protocolos.ProtocoloCliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import model.Controller;
import model.TrataServidores;
import protocolos.ProtocoloAplicacao;

/**
 *
 * @author Kayo
 */
public class Centralizador {

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(ProtocoloCliente.PORTA);
        System.out.println("Porta " + ProtocoloCliente.PORTA + " aberta.");
        System.out.println("Servidor Online.");

        Controller controller = new Controller();

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());//Responsável por receber mensagens do cliente.
            DataInputStream entrada = new DataInputStream(cliente.getInputStream());

            int tipoCliente = entrada.readInt();

            if (tipoCliente == ProtocoloAplicacao.CLIENTE) {
                System.out.println("Novo cliente:  " + cliente.getInetAddress().getHostAddress());

                StringBuilder pacote = new StringBuilder();
                pacote.append(ProtocoloCliente.IP_SERVIDOR); //Operação
                pacote.append(ProtocoloCliente.SEPARADOR);
                pacote.append(controller.getProximoServidor()); //IP do Servidor
                
                
                saida.writeUTF(pacote.toString());//Envia IP do servidor para o cliente
                cliente.close(); //Fecha a conexão com o cliente
            }else{
                String ip = cliente.getInetAddress().getHostAddress();
                controller.addServidor(ip, entrada, saida);
            }
        }
    }

}
