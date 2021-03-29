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
            clientSocket = serverSocket.accept();

            dos = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (in.ready()) {
                System.out.println(in.readLine());
            }

            File file = new File("sites/index.html");
            FileInputStream fis = new FileInputStream(file);

            dos.writeBytes(HttpOK);
            dos.writeBytes(contentTypeText);
            //dos.writeBytes("Content-Length: " + fis.available() + "\r\n");

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            fis.close();
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
