package fr.ul.miage.projet_reseau;

import fr.ul.miage.projet_reseau.webview.View200;
import fr.ul.miage.projet_reseau.webview.View400;
import fr.ul.miage.projet_reseau.webview.View404;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseRequest(DataOutputStream dos, String request, String hostName) throws IOException {
        if (request.startsWith("GET")) {
            String path = request.split(" ")[1];
            path = path.equals("/") ? "/index.html" : path;
            if (new File("sites/" + hostName + path).isFile()) {
                // Le fichier existe, on continue l'execution
                new View200(dos).sendResponse(hostName + path);
            } else {
                // Le fichier n'existe pas, on renvoie une erreur 404
                new View404(dos).sendResponse();
            }
            dos.flush();
        } else {
            // On n'a pas recu de requete GET, on renvoie une erreur 400
            new View400(dos).sendResponse();
        }
    }

}