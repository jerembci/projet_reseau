package fr.ul.miage.projet_reseau;

import fr.ul.miage.projet_reseau.webview.View200;
import fr.ul.miage.projet_reseau.webview.View400;
import fr.ul.miage.projet_reseau.webview.View404;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(80, 10, InetAddress.getByName("127.0.0.1"))) {
            while (true) {
                System.out.println("En attente d'une connexion...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion établie par l'IP " + clientSocket.getLocalAddress());

                // à chaque connexion d'un client, on créé un thread qui va gérer la requête
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
