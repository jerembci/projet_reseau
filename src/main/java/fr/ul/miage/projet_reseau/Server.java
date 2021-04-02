package fr.ul.miage.projet_reseau;

import java.net.*;
import java.io.*;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataOutputStream dos;
    private BufferedReader in;

    private final String HttpOK = String.format("HTTP/1.1 200 OK%n");
    private final String contentTypeText = String.format("Content-Type: text/html%n");

    private static final int PORT = 80;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port, 10, InetAddress.getByName("127.0.0.1"));
        while (true) {
            System.out.println("En attente d'une connexion...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexion établie par l'IP " + clientSocket.getLocalAddress());
            dos = new DataOutputStream(clientSocket.getOutputStream());
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

            // TODO: Vérifier que c'est du GET
            String path = requete.split(" ")[1];
            if (!path.contains("favicon.ico")) {
                if (path.equals("/")) {
                    path = "/index.html";
                }

                File file = new File("sites/" + hostName + path);
                FileInputStream fis = new FileInputStream(file);

                dos.writeBytes(HttpOK);
                dos.writeBytes(contentTypeText);
                dos.writeBytes(String.format("Content-Length: %d%n", fis.available()));
                dos.write(fis.readAllBytes());
                fis.close();
            }
            dos.close();
        }
    }

    public void stop() throws IOException {
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
