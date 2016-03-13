/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dcandrade
 */
public class ServidorAplicacao implements Comparable<ServidorAplicacao>{

    private String ip;
    private Integer conexoes;

    public ServidorAplicacao(String ip) {
        this.ip = ip;
        this.conexoes = 0;
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
