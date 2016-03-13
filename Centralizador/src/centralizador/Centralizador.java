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
public class Centralizador {

    private static int ServerID = 0;
    private static int PORT = 8801;

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(ClientProtocol.PORTA);
        System.out.println("Porta " + ClientProtocol.PORTA + " aberta.");
        System.out.println("Servidor Online.");

        Controller controller = new Controller();

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());//Responsável por receber mensagens do cliente.
            DataInputStream entrada = new DataInputStream(cliente.getInputStream());

            int tipoCliente = entrada.readInt();

            if (tipoCliente == ClientProtocol.IM_A_CLIENT) {
                System.out.println("Novo cliente:  " + cliente.getInetAddress().getHostAddress());
                IPTuple server = controller.getProximoServidor();
                StringBuilder pacote = new StringBuilder();
                pacote.append(ClientProtocol.IP_SERVIDOR); //Operação
                pacote.append(ClientProtocol.SEPARADOR);
                pacote.append(server.getIP()); //IP do Servidor
                pacote.append(ClientProtocol.SEPARADOR);
                pacote.append(server.getPort()); //Porta do Servidor

                saida.writeUTF(pacote.toString());//Envia IP do servidor para o cliente
                cliente.close(); //Fecha a conexão com o cliente
            } else if(tipoCliente == ServerProtocol.ITS_A_SERVER) {
                System.out.println("Servidor conectado");
                saida.writeInt(Centralizador.ServerID++);
                int port = Centralizador.PORT++;
                saida.writeInt(port);
                String ip = cliente.getInetAddress().getHostAddress();
                controller.addServidor(ip, port, entrada, saida);
                System.out.println("Total de Servidores: " + controller.amountOfServers());
            }
        }
    }

}
