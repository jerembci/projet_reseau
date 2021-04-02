package fr.ul.miage.projet_reseau;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;

    private final String HttpOK = String.format("HTTP/1.1 200 OK%n");

    private static final int PORT = 80;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port, 10, InetAddress.getByName("127.0.0.1"));
        while (true) {
            System.out.println("En attente d'une connexion...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexion établie par l'IP " + clientSocket.getLocalAddress());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
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

                    File file = new File("sites/" + hostName + path);
                    try (FileInputStream fis = new FileInputStream(file)) {

                        dos.writeBytes(HttpOK);
                        dos.writeBytes(setContentType(path));
                        dos.writeBytes(String.format("Content-Length: %d%n", fis.available()));
                        dos.writeBytes("\r\n");
                        dos.write(fis.readAllBytes());
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                clientSocket.shutdownOutput();
                dos.flush();
                dos.close();
            }
        }
    }

    private String setContentType(String file) {
        String extension = file.substring(file.lastIndexOf("."));
        switch (extension) {
            case ".html":
            case ".htm":
                return "Content-Type: text/html\r\n";
            case ".css":
            case ".sass":
                return "Content-Type: text/css\r\n";
            case ".js":
            case ".min.js":
                return "Content-Type: application/javascript\r\n";
            case ".jpg":
            case ".jpeg":
                return "Content-Type: image/jpeg\r\n";
            case ".png":
                return "Content-Type: image/png\r\n";
            case ".gif":
                return "Content-Type: image/gif\r\n";
            default:
                return "Content-Type:\r\n";
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
