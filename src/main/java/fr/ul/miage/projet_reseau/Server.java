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

                try (DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        String request = in.readLine();
                        String host = "";

                        // Parsing du header
                        while (in.ready()) {
                            String line = in.readLine();
                            if (line.toUpperCase().startsWith("HOST")) {
                                host = line;
                            }
                        }

                        System.out.println("Requête : " + request);

                        String hostName = host.split(" ")[1];
                        int dotIndex = hostName.indexOf(".");
                        hostName = hostName.substring(0, dotIndex);

                        System.out.println("Host : " + hostName);
                        System.out.println();

                        parseRequest(dos, request, hostName);
                    }
                }
            }
        }
    }

    private void parseRequest(DataOutputStream dos, String request, String hostName) throws IOException {
        if (request.startsWith("GET")) {
            String path = request.split(" ")[1];
            if (!path.contains("favicon.ico")) {
                path = path.equals("/") ? "/index.html" : path;
                if (new File("sites/" + hostName + path).isFile()) {
                    // Le fichier existe, on continue l'execution
                    new View200(dos).sendResponse(hostName + path);
                } else {
                    // Le fichier n'existe pas, on renvoie une erreur 404
                    new View404(dos).sendResponse();
                }
            }
            dos.flush();
        } else {
            // On n'a pas recu de requete GET, on renvoie une erreur 400
            new View400(dos).sendResponse();
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
