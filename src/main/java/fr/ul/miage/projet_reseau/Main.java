package fr.ul.miage.projet_reseau;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final int port = 80;
    private static final String HttpOK = "HTTP/1.1 200 OK\r\n";
    private static final String contentTypeText = "Content-Type: text/html" + "\r\n";
    private static final String contentTypeImage = "Content-Type: image/gif" + "\r\n";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("En attente de connexion sur le port " + port);
        Socket socket = serverSocket.accept();

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
        String inputLine = br.readLine();
        System.out.println(inputLine);

//
//
//        out.println(HttpOK);
//        out.flush();
//        br.close();
//        serverSocket.close();
//
//        InputStream fichier = new DataInputStream(Main.class.getResourceAsStream("index.html"));
//        dos.writeBytes(HttpOK);
//        dos.writeBytes(contentTypeText);
//        dos.write(fichier.readAllBytes());

    }
}
