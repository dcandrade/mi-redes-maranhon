/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author dcandrade
 */
public class ApplicationServer implements Comparable<ApplicationServer>{

    private final String ip;
    private final int port;
    private Integer connections;
    private final DataInputStream input;
    private final DataOutputStream output;

    public ApplicationServer(String ip, int port, DataInputStream input, DataOutputStream output) {
        this.ip = ip;
        this.port = port;
        this.input = input;
        this.output = output;
        this.connections = 0;
    }
    
    public void sendData(String dado) throws IOException{
        this.output.writeUTF(dado);
    }
    
    public String recieveData() throws IOException{
        return this.input.readUTF();
    }
    

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Integer getConexoes() {
        return this.connections;
    }

    public void incrementConnections() {
        this.connections++;
    }

    public void decrementConnections() {
        this.connections--;
    }

    @Override
    public int compareTo(ApplicationServer o) {
        //Compara o numero de conexões
        int compare = this.connections.compareTo(o.connections);
        
        //Se for igual compara o IP
        return compare != 0? compare : this.ip.compareTo(o.ip);
    }

    @Override
    public String toString() {
        return this.ip + ": "+this.connections;
    }
    
    
}
