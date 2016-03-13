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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import model.ServidorAplicacao;
import model.TrataServidores;
import protocolos.ProtocoloAplicacao;

/**
 *
 * @author Kayo
 */
public class Centralizador {

    public static void main(String[] args) throws IOException {
        List<ServidorAplicacao> servidoresAplicacao = new LinkedList();
        ServerSocket servidor = new ServerSocket(ProtocoloCliente.PORTA);
        System.out.println("Porta "+ProtocoloCliente.PORTA+" aberta!");

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente "
                    + cliente.getInetAddress().getHostAddress()
            );

            DataInputStream entrada = new DataInputStream(cliente.getInputStream());//Responsável por receber mensagens do cliente.
            DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());//Responsável por receber mensagens do cliente.

            int opcoes = entrada.readInt(); //verifica se a conexão é de um servidor (0) ou cliente (1)

            if (opcoes == 0) {//se um servidor conectou

                ServidorAplicacao novo = new ServidorAplicacao(cliente.getInetAddress().getHostAddress());
                servidoresAplicacao.add(novo);
              //Collections.sort(servidoresAplicacao, new ServidorAplicacao(null));//ordena a lista com a quantidade de conexões ativas
                TrataServidores ts = new TrataServidores(servidoresAplicacao);//alterarValores de conexão ativas/desativas e verificar servidor online
                saida.writeInt(cliente.getPort() + 1);//envia ao servidor sua porta de conexão com os clientes
                System.out.println("Aqui");

                new Thread(ts).start();
            } else if (opcoes == 1) { //se um cliente conectou
                System.out.println("aaa");
                saida.writeUTF(servidoresAplicacao.get(0).getIp().concat("/").
                        concat(Integer.toString(ProtocoloAplicacao.PORT))); //envia ip/porta ex.: 192.168.0.1/12345
              
                //  servidoresAplicacao.get(0).novaConexaoAtiva();//atualiza a quantidade de conexões ativas
                System.out.println("abb");

                //Collections.sort(servidoresAplicacao, new ServidorAplicacao(null, 0));//ordena a lista com a quantidade de conexões ativas
            }

        }

    }

}
