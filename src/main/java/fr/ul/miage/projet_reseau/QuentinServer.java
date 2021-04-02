package fr.ul.miage.projet_reseau;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class QuentinServer extends Thread {

    static final String HTML_START =
            "<html> <title>HTTP Server in java</title> <body>";

    static final String HTML_END = "</body> </html>";

    Socket clientSocket = null;
    BufferedReader in = null;
    DataOutputStream out = null;

    public QuentinServer(Socket client) {
        clientSocket = client;
    }

    public void run() {
        try {
            System.out.println("The Client " + clientSocket.getInetAddress()
                    + ":" + clientSocket.getPort() + " is connected");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());

            String requestString = in.readLine();
            String headerLine = requestString;

            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            System.out.println(httpMethod);
            String httpQueryString = tokenizer.nextToken();

            StringBuffer responseBuffer = new StringBuffer();
            responseBuffer
                    .append("<b> This is the HTTP Server Home Page.... </b><BR>");
            responseBuffer.append("The HTTP Client request is ....<BR>");

            System.out.println("The HTTP request string is ....");
            while (in.ready()) {
                // Read the HTTP complete HTTP Query
                responseBuffer.append(requestString + "<BR>");
                System.out.println(requestString);
                requestString = in.readLine();
            }

            if (httpMethod.equals("GET")) {
                if (httpQueryString.equals("/")) {
                    // The default home page
                    sendResponse(200, responseBuffer.toString(), false);
                } else {
                    // This is interpreted as a file name
                    String fileName = httpQueryString.replaceFirst("/", "");
                    if (new File(fileName).isFile()) {
                        sendResponse(200, fileName, true);
                    } else {
                        sendResponse(
                                404,
                                "<b>The Requested resource not found ...."
                                        + "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>",
                                false);
                    }
                }
            } else
                sendResponse(
                        404,
                        "<b>The Requested resource not found ...."
                                + "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>",
                        false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(int statusCode, String responseString,
                             boolean isFile) throws Exception {

        String statusLine = null;
        String serverdetails = "Server: Java HTTPServer";
        String contentLengthLine = null;
        String fileName = null;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";
        FileInputStream fis = null;

        if (statusCode == 200)
            statusLine = "HTTP/1.1 200 OK" + "\r\n";
        else
            statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

        if (isFile) {
            fileName = responseString;
            fis = new FileInputStream(fileName);
            contentLengthLine = "Content-Length: "
                    + Integer.toString(fis.available()) + "\r\n";
            if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
                contentTypeLine = "Content-Type: \r\n";
        } else {
            responseString = QuentinServer.HTML_START + responseString
                    + QuentinServer.HTML_END;
            contentLengthLine = "Content-Length: " + responseString.length()
                    + "\r\n";
        }

        out.writeBytes(statusLine);
        out.writeBytes(serverdetails);
        out.writeBytes(contentTypeLine);
        out.writeBytes(contentLengthLine);
        out.writeBytes("Connection: close\r\n");
        out.writeBytes("\r\n");

        if (isFile)
            sendFile(fis, out);
        else
            out.writeBytes(responseString);

        out.close();
    }

    public void sendFile(FileInputStream fis, DataOutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fis.close();
    }

    public static void main(String args[]) throws Exception {

        ServerSocket Server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
        System.out.println("TCPServer Waiting for client on port 5000");

        while (true) {
            Socket connected = Server.accept();
            (new QuentinServer(connected)).start();
        }
    }
}