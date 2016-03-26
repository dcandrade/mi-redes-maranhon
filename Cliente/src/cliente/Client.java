/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import protocols.Protocol;

/**
 *
 * @author Kayo
 * @author Daniel Andrade
 */
public class Client {
    DataInputStream input;
    DataOutputStream output;

    /**
     *
     * @return A string array. [0] = IP, [1] = Port
     * @throws IOException
     */
    private String[] askForServer() throws IOException {
        System.out.println("Solicitando IP ao Redirecionador....");
        Socket socket = new Socket(Protocol.SERVER, Protocol.PORT);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        out.writeUTF(Protocol.IM_A_CLIENT);
        String response = in.readUTF();
        System.out.println(response);
        String[] data = response.split(Protocol.SEPARATOR);
        System.out.println("Recebido: " + data[0] + ":" + data[1]);
        return data;
    }
   
    public void connectToServer(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Conectado ao servidor.");
    }

    public void connect() throws IOException {
        String[] server = this.askForServer();
        this.connectToServer(server[0], Integer.parseInt(server[1]));
    }

    public void disconnect() throws IOException {
        this.input.close();
        this.output.close();
    }

    public List<Book> getBooks() throws IOException {
        String response = this.sendRequest(Protocol.SHOWMETHEBOOKS);
        
        StringTokenizer tokens = new StringTokenizer(response, Protocol.SEPARATOR);
        List<Book> books = new LinkedList<>();
        while (tokens.hasMoreTokens()) {
            String name = tokens.nextToken();
            int amount = Integer.parseInt(tokens.nextToken());
            double value = Double.parseDouble(tokens.nextToken());
            books.add(new Book(name, amount, value));
        }
        
        return books;
    }
    
    public boolean buyBook(String name, int amount) throws IOException{
        StringBuilder request = new StringBuilder();
        request.append(Protocol.GIVEMETHEBOOKS).append(Protocol.SEPARATOR);
        request.append(name).append(Protocol.SEPARATOR);
        request.append(amount);
        return Boolean.parseBoolean(this.sendRequest(request.toString()));
    }

    private String sendRequest(String request) throws IOException {
        this.output.writeUTF(request);
        this.output.flush();
        return this.input.readUTF();
    }

    private String sendRequest(int request) throws IOException {
        return this.sendRequest("" + request);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();

        client.connect();
        System.out.println("Solicitando livros...");
        List<Book> books = client.getBooks();
        System.out.println("Livros recebidos");
        client.disconnect();
        System.out.println("Desconectado do servidor");
        
        for(Book b:books){
            System.out.println(b);
        }

        client.connect();
        System.out.println("Solcitando a compra de 5 HarryPorco");
        client.buyBook("HarryPorco", 5);
        System.out.println("Compra realizada");
        client.disconnect();

    }

}
