/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import exceptions.NoLastPurchaseException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.security.auth.login.LoginException;
import protocols.Protocol;

/**
 *
 * @author Kayo
 * @author Daniel Andrade
 */
public class Client {

    private DataInputStream input;
    private DataOutputStream output;
    private final String username;
    private final String password;

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    private void connectToServer(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Conectado ao servidor.");
    }

    public boolean register() throws IOException, LoginException {
        this.connect();

        String request = Protocol.REGISTER + Protocol.SEPARATOR + this.password;
        String response = this.sendRequest(request);

        this.disconnect();

        return Boolean.parseBoolean(response);
    }

    private void connect() throws IOException {
        String[] server = this.askForServer();
        this.connectToServer(server[0], Integer.parseInt(server[1]));
    }

    private void disconnect() throws IOException {
        this.input.close();
        this.output.close();
    }

    public List<Book> getBooks() throws IOException, LoginException {
        this.connect();
        String response = this.sendRequest(Protocol.SHOWMETHEBOOKS);
        this.disconnect();
        
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

    public boolean buyBook(String name, int amount) throws IOException, LoginException {
        StringBuilder request = new StringBuilder();
        request.append(Protocol.GIVEMETHEBOOKS).append(Protocol.SEPARATOR);
        request.append(name).append(Protocol.SEPARATOR);
        request.append(amount);
        
        this.connect();
        boolean result =  Boolean.parseBoolean(this.sendRequest(request.toString()));
        this.disconnect();
        
        return result;
    }

    public Purchase getLastPurchase() throws IOException, LoginException, NoLastPurchaseException {
        this.connect();
        String purchase = this.sendRequest(Protocol.LAST_PURCHASE);
        this.disconnect();
        if(purchase.equals("false")){
            throw new NoLastPurchaseException();
        }
        return Purchase.unserialize(purchase);
    }

    private String sendRequest(String request) throws IOException, LoginException {
        String realRequest = this.username + Protocol.SEPARATOR
                + this.password + Protocol.SEPARATOR + request;
        this.output.writeUTF(realRequest);
        this.output.flush();

        String response = this.input.readUTF();
        if (response.equals(Protocol.LOGIN_FAILED)) {
            throw new LoginException();
        }

        return response;
    }

    public static void main(String[] args) throws IOException, LoginException, NoLastPurchaseException {
        Client client = new Client("Daniel", "12345");


        System.out.println("Solicitando livros...");
        List<Book> books = client.getBooks();
        System.out.println("Livros recebidos");
        System.out.println("Desconectado do servidor");

        for (Book b : books) {
            System.out.println(b);
        }

        System.out.println("Solcitando a compra de 5 HarryPorco");
        System.out.println("Resultado da compra: "+client.buyBook("Livro - O Livro das Criaturas de Harry Potter", 10));
        System.out.println("Compra realizada");
        System.out.println("\n");
        System.out.println(client.getLastPurchase());

    }

}
