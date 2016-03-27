/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocolos.ServerProtocol;

/**
 *
 * @author dcandrade
 */
public class ServerRoom implements Runnable{
    private final DataInputStream input;
    private final DataOutputStream output;
    private final String ip;
    private final Controller controller;

    public ServerRoom(Controller controller, DataInputStream input, DataOutputStream output, String ip) {
        this.controller = controller;
        this.input = input;
        this.output = output;
        this.ip = ip;
    }
    

    @Override
    public void run() {
        try {
            StringTokenizer request = new StringTokenizer(this.input.readUTF(), ServerProtocol.SEPARATOR);
            
            String operation = request.nextToken();
            
            switch(operation){
                case ServerProtocol.START_SERVER:
                    int port  = Integer.parseInt(request.nextToken());
                    this.controller.addServer(ip, port, input, output);
                    System.out.println("Servidor iniciado: "+ ip+":"+port);
                    System.out.println("Total de Servidores: " + controller.amountOfServers());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
