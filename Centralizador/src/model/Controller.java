/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.IPTuple;

/**
 * Gerencia os servidores de aplicação
 *
 * @author Kayo
 * @author Daniel Andrade
 */
public class Controller {

    //Mantém os servidores ordenados pelo critério de num de conexões
    private final List<ApplicationServer> orderOfServers;
    //Mapeia o IP para o numero de conexoes
    private final Map<String, ApplicationServer> mapOfServers;

    public Controller() {
        this.orderOfServers = new ArrayList<>();
        this.mapOfServers = new TreeMap<>();
    }

    public void addServer(String ip, int port, DataInputStream input, DataOutputStream output) {
        ApplicationServer server = new ApplicationServer(ip, port, input, output);
        this.orderOfServers.add(server);
        this.mapOfServers.put(ip, server);
        this.sortServers();

        ServerWatcher watcher = new ServerWatcher(server, this);
        watcher.start();
    }

    public void removeServer(String ip) {
        ApplicationServer server = this.mapOfServers.remove(ip);
        this.orderOfServers.remove(server);
    }

    public void decrementConnectionsOnServer(String ip) {
        this.mapOfServers.get(ip).decrementConnections();
        this.sortServers();
    }

    public int amountOfServers() {
        return this.orderOfServers.size();
    }

    public IPTuple getProximoServidor() {
        ApplicationServer server = this.orderOfServers.get(0);
        server.incrementConnections();
        this.sortServers();
        
        return new IPTuple(server.getIp(), server.getPort());
        
    }

    public Iterator<ApplicationServer> getServidores() {
        return this.orderOfServers.iterator();
    }

    public int getNumConexoes(String ip) {
        return this.mapOfServers.get(ip).getConexoes();
    }

    private void sortServers() {
        Collections.sort(this.orderOfServers);
    }

    public static void main(String[] args) {
        Controller c = new Controller();

        c.addServer("S1", 0, null, null);
        c.addServer("S2", 0, null, null);
        c.addServer("S3", 0, null, null);

        c.getProximoServidor();
        c.getProximoServidor();
        c.getProximoServidor(); //Ao fina disso cada um deve estar com 1

        Iterator<ApplicationServer> server = c.getServidores();
        while (server.hasNext()) {
            System.out.println(server.next());
        }

        System.out.println("---");

        c.getProximoServidor();
        c.getProximoServidor();
        c.getProximoServidor();

        c.getProximoServidor();
        c.getProximoServidor();
        c.getProximoServidor(); //Ao fina disso cada um deve estar com 3

        c.decrementConnectionsOnServer("S2");
        c.decrementConnectionsOnServer("S1");

        server = c.getServidores();
        while (server.hasNext()) {
            System.out.println(server.next());
        }

        System.out.println("---");

        c.decrementConnectionsOnServer("S2");

        server = c.getServidores();
        while (server.hasNext()) {
            System.out.println(server.next());
        }

    }

}

