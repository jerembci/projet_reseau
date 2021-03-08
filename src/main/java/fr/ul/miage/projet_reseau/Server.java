package fr.ul.miage.projet_reseau;

import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args)
            throws IOException {
        String requete;
        String[] parts;
        InputStream lien;
        byte[] content;
        DataInputStream fichier;
        String HttpOK = "HTTP/1.1 200 OK\r\n";
        String contentTypeText = "Content-Type: text/html" + "\r\n";
        String contentTypeImage = "Content-Type: image/gif" + "\r\n";
        String contentLengthLine;
        ServerSocket srv;
        System.out.println("CTRL + C pour arreter le serveur !");
        // S'il n'y a pas d'arguments (ou plus d'un) on sera sur le port 80
        if (args.length != 1) {
            srv = new ServerSocket(80);
        } else {
            srv = new ServerSocket(Integer.parseInt(args[0]));
        }
        System.out.println("En attente d'une connexion sur le port " + srv.getLocalPort() + "...");
        Socket socket = srv.accept();
        System.out.println("Connexion du client " + socket.getInetAddress());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        while (true) {
            // On lit les requetes du client
            requete = br.readLine();
            if (requete == null) {
                // Si la lecture rend null alors on attend un nouveau client et on reprend la
                // lecture
                System.out.println("En attente d'une connexion...");
                socket = srv.accept();
                System.out.println("Connexion du client " + socket.getInetAddress());
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                dos = new DataOutputStream(socket.getOutputStream());
                continue;
            } else {
                System.out.println(requete);
                parts = requete.split(" ");
                // Si la requete est une requete GET alors on va retourner quelque chose
                if (parts[0].equals("GET")) {
                    // Si le client demande un favicon on ne fait rien et on continue la lecture
                    // (pas de favicon fourni dans le dossier SiteWeb)
                    if (parts[1].equals("/favicon.ico")) {
                        System.out.println("Pas de favicon");
                        continue;
                    }
                    // Si aucun fichier n'est demande on va retourner index.html
                    if (parts[1].equals("/")) {
                        lien = Server.class.getResourceAsStream("index.html");
                    } else {
                        // Sinon on va retourner le fichier demande
                        lien = Server.class.getResourceAsStream(parts[1]);
                    }
                    // On va chercher le fichier et on le lit
                    fichier = new DataInputStream(lien);
                    contentLengthLine = "Content-Length: " + Integer.toString(fichier.available()) + "\r\n";
                    content = fichier.readAllBytes();
                    dos.writeBytes(HttpOK);
                    // On gere les differents types de fichiers qui peuvent etre demandes
                    if (parts[1].contains(".html"))
                        dos.writeBytes(contentTypeText);
                    if (parts[1].contains(".gif") || parts[1].contains(".img"))
                        dos.writeBytes(contentTypeImage);
                    dos.writeBytes(contentLengthLine);
                    dos.writeBytes("\r\n");
                    dos.write(content);
                    dos.flush();
                    fichier.close();
                }
            }
        }
    }
}
