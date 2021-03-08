package fr.ul.miage.projet_reseau;

import java.net.*;
import java.io.*;
import java.util.stream.Collectors;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private final String HttpOK = String.format("HTTP/1.1 200 OK%n");
    private static final String contentTypeText = "Content-Type: text/html" + "\r\n";

    private static final int PORT = 80;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

//        InputStream file = new DataInputStream(Main.class.getResourceAsStream("index.html"));
//        String contentLengthLine = "Content-Length: " + file.available() + "\r\n";
//        out.println(HttpOK);
//        out.println(contentTypeText);
//        out.println(Arrays.toString(fichier.readAllBytes()));
//        dos.writeBytes(HttpOK);
//        dos.writeBytes(contentTypeText);
//        dos.writeBytes(contentLengthLine);
//        dos.writeBytes("\r\n");
//        dos.write(file.readAllBytes());
//        dos.flush();

        InputStream file = Server.class.getResourceAsStream("index.html");
        String s = new BufferedReader(new InputStreamReader(file)).lines().collect(Collectors.joining("\r\n"));
        dos.writeBytes("HTTP/1.1 200 OK");
        dos.writeBytes("Content-type: text/html");
        dos.writeBytes("\r\n");
        dos.writeBytes(s);
//        out.println("HTTP/1.1 200 OK");
//        out.println("Content-type: text/html");
//        out.println("\r\n");
//        out.println(s);
        dos.flush();
        dos.close();
    }

    public void stop() throws IOException {
        in.close();
        out.close();
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
