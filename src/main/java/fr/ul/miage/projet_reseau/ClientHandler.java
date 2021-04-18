package fr.ul.miage.projet_reseau;

import fr.ul.miage.projet_reseau.webview.*;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final String resourcesPath = "sites/";
    private String auth = "";

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
                System.out.println(line);
                if (line.toUpperCase().startsWith("HOST:")) {
                    host = line;
                }
                if (line.toUpperCase().startsWith("AUTHORIZATION:")) {
                    auth = line;
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
            if (new File(resourcesPath + hostName + path).isFile()) {
                // Le fichier existe, on continue l'exécution
                File authFile;
                if ((authFile = new File(resourcesPath + hostName + "/.htpasswd")).exists()) {
                    if (!auth.equals("")) {
                        if (checkCredentials(auth, authFile)) {
                            new View200(dos).sendResponse(resourcesPath, hostName + path);
                        } else {
                            new View403(dos).sendResponse();
                        }
                    } else {
                        new View401(dos).sendResponse();
                    }
                } else {
                    new View200(dos).sendResponse(resourcesPath, hostName + path);
                }
            } else {
                // Le fichier n'existe pas, on renvoie une erreur 404
                new View500(dos).sendResponse();
            }
            dos.flush();
        } else if (request.startsWith("POST")) {
            dos.writeBytes("POST");
            System.out.println("REQUÊTE POST");
        } else {
            // On n'a pas recu de requete GET ni POST, on renvoie une erreur 400
            new View400(dos).sendResponse();
        }
    }

    private boolean checkCredentials(String auth, File authFile) {
        String encodedCredentials = auth.split(" ")[2];
        String decodedString = new String(Base64.getDecoder().decode(encodedCredentials));
        System.out.println(decodedString);
        return false;
    }

}