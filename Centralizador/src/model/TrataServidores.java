/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Kayo
 */
public class TrataServidores implements Runnable {
    LinkedList<ServidorAplicacao> servidoresAplicacao;
    
    public TrataServidores(LinkedList<ServidorAplicacao> servidoresAplicacao) {
        this.servidoresAplicacao=servidoresAplicacao;
    }

    @Override
    public void run() {

        //servidoresAplicacao.add(new ServidorAplicacao("192.111.1.0", 12345));
        //Collections.sort(servidoresAplicacao, new ServidorAplicacao(null,0));
        
    }
    
}
