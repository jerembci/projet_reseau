package fr.ul.miage.projet_reseau;

import fr.ul.miage.projet_reseau.webview.View;
import fr.ul.miage.projet_reseau.webview.View200;
import fr.ul.miage.projet_reseau.webview.View400;
import fr.ul.miage.projet_reseau.webview.View404;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;

    public void start() throws IOException {
        serverSocket = new ServerSocket(80, 10, InetAddress.getByName("127.0.0.1"));
        while (true) {
            System.out.println("En attente d'une connexion...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexion établie par l'IP " + clientSocket.getLocalAddress());

            try (DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String requete = in.readLine();
                String host = in.readLine();
                while (in.ready()) {
                    in.readLine();
                }

                System.out.println("Requête : " + requete);

                String hostName = host.split(" ")[1];
                int dotIndex = hostName.indexOf(".");
                hostName = hostName.substring(0, dotIndex);

                System.out.println("Host : " + hostName);
                System.out.println();

                if (requete.startsWith("GET")) {
                    String path = requete.split(" ")[1];
                    if (!path.contains("favicon.ico")) {
                        if (path.equals("/")) {
                            path = "/index.html";
                        }
                        if (new File("sites/" + hostName + path).isFile()) {
                            // Le fichier existe, on continue l'execution
                            new View200(path, dos).sendResponse(hostName);
                        } else {
                            // Le fichier n'existe pas, on renvoie une erreur 404
                            new View404(dos).sendResponse(hostName);
                        }
                    }
                    dos.flush();
                } else {
                    // On n'a pas recu de requete GET, on renvoie une erreur 400
                    new View400(dos).sendResponse(hostName);
                }
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
