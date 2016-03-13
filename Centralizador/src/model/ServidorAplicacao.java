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
class ServidorAplicacao implements Comparable<ServidorAplicacao>{

    private final String ip;
    private Integer conexoes;
    private final DataInputStream entrada;
    private final DataOutputStream saida;

    public ServidorAplicacao(String ip, DataInputStream entrada, DataOutputStream saida) {
        this.ip = ip;
        this.entrada = entrada;
        this.saida = saida;
        this.conexoes = 0;
    }
    
    public void enviarDado(String dado) throws IOException{
        this.saida.writeUTF(dado);
    }
    
    public String receberDado() throws IOException{
        return this.entrada.readUTF();
    }
    

    public String getIp() {
        return ip;
    }

    public Integer getConexoes() {
        return conexoes;
    }

    public void incrementarConexoes() {
        this.conexoes++;
    }

    public void decrementarConexoes() {
        this.conexoes--;
    }

    @Override
    public int compareTo(ServidorAplicacao o) {
        //Compara o numero de conexões
        int compare = this.conexoes.compareTo(o.conexoes);
        
        //Se for igual compara o IP
        return compare != 0? compare : this.ip.compareTo(o.ip);
    }

    @Override
    public String toString() {
        return this.ip + ": "+this.conexoes;
    }
    
    
}
