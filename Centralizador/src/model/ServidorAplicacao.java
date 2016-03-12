/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author Kayo
 */
public class ServidorAplicacao implements Comparator<ServidorAplicacao>{
    private int conexaoAtiva;
    private String ip;
    private int porta;
    
    public ServidorAplicacao (String ip, int porta){
        this.ip = ip;
        this.porta = porta;
    }

   
    public int getConexaoAtiva(){
        return conexaoAtiva;
    }
    public void setConexaoAtiva(int valor){
        this.conexaoAtiva=valor;
    }
    public String getIp(){
        return this.ip;
    }
    public int getPorta(){
        return this.porta;
    }
    
    public void novaConexaoAtiva() {
        this.conexaoAtiva=conexaoAtiva+1;
    }
    
    
    @Override
    public int compare(ServidorAplicacao t, ServidorAplicacao t1) {
        if(t.getConexaoAtiva()>t1.getConexaoAtiva()){
            return -1;
        }
        else if (t.getConexaoAtiva()<t1.getConexaoAtiva()){
            return 1;
        }
        else
            return 0;
    }


    public static void main(String[] args){
        ServidorAplicacao a= new ServidorAplicacao("192.168.1.1",12345);
        ServidorAplicacao b= new ServidorAplicacao("192.168.1.2",12344);
        ServidorAplicacao c= new ServidorAplicacao("192.168.1.3",12346);
        ServidorAplicacao d= new ServidorAplicacao("192.168.1.4",12347);
        ServidorAplicacao e= new ServidorAplicacao("192.168.1.5",12345);

        a.setConexaoAtiva(5);
        b.setConexaoAtiva(6);
        c.setConexaoAtiva(2);
        d.setConexaoAtiva(5);

        LinkedList<ServidorAplicacao> f=new LinkedList();
        f.add(a);
        f.add(b);
        f.add(c);
        f.add(d);
        f.add(e);
        System.out.println("Lista Normal");
        for (int i=0; i<f.size(); i++){
            System.out.println(f.get(i).getIp());
            System.out.println(f.get(i).getConexaoAtiva());
            System.out.println();

        }
                    System.out.println();

        Collections.sort(f, new ServidorAplicacao(null, 12345));

                
         System.out.println("Ordenada");
        for (int i=0; i<f.size(); i++){
            System.out.println(f.get(i).getIp());
            System.out.println(f.get(i).getConexaoAtiva());
            System.out.println();

        }
    }


}
    
