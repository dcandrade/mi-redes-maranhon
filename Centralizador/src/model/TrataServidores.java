/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Kayo
 */
public class TrataServidores implements Runnable {

    List<ServidorAplicacao> servidoresAplicacao;

    public TrataServidores(List<ServidorAplicacao> servidoresAplicacao) {
        this.servidoresAplicacao = servidoresAplicacao;
    }

    @Override
    public void run() {

        //servidoresAplicacao.add(new ServidorAplicacao("192.111.1.0", 12345));
        //Collections.sort(servidoresAplicacao, new ServidorAplicacao(null,0));
    }

}
