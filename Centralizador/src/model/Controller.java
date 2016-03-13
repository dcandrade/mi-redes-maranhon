/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Gerencia os servidores de aplicação
 *
 * @author Kayo
 * @author Daniel Andrade
 */
public class Controller {

    //Mantém os servidores ordenados pelo critério de num de conexões
    private List<ServidorAplicacao> ordemServidores;
    //Mapeia o IP para o numero de conexoes
    private TreeMap<String, ServidorAplicacao> mapaServidores;

    public Controller() {
        this.ordemServidores = new ArrayList<>();
        this.mapaServidores = new TreeMap<>();
    }

    public void addServidor(String ip) {
        ServidorAplicacao servidor = new ServidorAplicacao(ip);
        this.ordemServidores.add(servidor);
        this.mapaServidores.put(ip, servidor);
        this.ordenarServidores();
    }

    public void removerServidor(String ip) {
        ServidorAplicacao servidor = this.mapaServidores.remove(ip);
        this.ordemServidores.remove(servidor);
    }

    public void decrementarConexoesServidor(String ip) {
        this.mapaServidores.get(ip).decrementarConexoes();
        this.ordenarServidores();
    }

    public String getProximoServidor() {
        ServidorAplicacao servidor = this.ordemServidores.get(0);
        servidor.incrementarConexoes();
        this.ordenarServidores();

        return servidor.getIp();
    }

    public Iterator<ServidorAplicacao> getServidores() {
        return this.ordemServidores.iterator();
    }

    public int getNumConexoes(String ip) {
        return this.mapaServidores.get(ip).getConexoes();
    }

    private void ordenarServidores() {
        Collections.sort(this.ordemServidores);
    }

    public static void main(String[] args) {
        Controller c = new Controller();

        c.addServidor("S1");
        c.addServidor("S2");
        c.addServidor("S3");

        c.getProximoServidor();
        c.getProximoServidor();
        c.getProximoServidor(); //Ao fina disso cada um deve estar com 1

        Iterator<ServidorAplicacao> servidores = c.getServidores();
        while (servidores.hasNext()) {
            System.out.println(servidores.next());
        }

        System.out.println("---");

        c.getProximoServidor();
        c.getProximoServidor();
        c.getProximoServidor();

        c.getProximoServidor();
        c.getProximoServidor();
        c.getProximoServidor(); //Ao fina disso cada um deve estar com 3

        c.decrementarConexoesServidor("S2");
        c.decrementarConexoesServidor("S1");

        servidores = c.getServidores();
        while (servidores.hasNext()) {
            System.out.println(servidores.next());
        }

        System.out.println("---");

        c.decrementarConexoesServidor("S2");

        servidores = c.getServidores();
        while (servidores.hasNext()) {
            System.out.println(servidores.next());
        }

    }

}
