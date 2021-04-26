package fr.ul.miage.projet_reseau;

import fr.ul.miage.projet_reseau.webview.View200;
import fr.ul.miage.projet_reseau.webview.View400;
import fr.ul.miage.projet_reseau.webview.View404;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

@Slf4j
public class Server {

    private static int port;
    private static final String PROPERTIES_FILENAME = "config.properties";
    private static String webroot;

    /**
     * Parsing du fichier de configuration .properties pour récupérer le port TCP et le répertoire racine des sites web
     */
    public static void parseProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(PROPERTIES_FILENAME)) {
            properties.load(inputStream);
            port = Integer.parseInt(properties.getProperty("port"));
            webroot = properties.getProperty("webroot");
        } catch (FileNotFoundException e) {
            log.error(String.format("Property file '%s' not found in the classpath.", PROPERTIES_FILENAME));
            System.exit(1);
        } catch (IOException e) {
            log.error(String.format("Could not open file '%s'", PROPERTIES_FILENAME));
            log.error("Exception: " + e);
            System.exit(1);
        } catch (NumberFormatException e) {
            log.error("Le numéro du port doit être un entier");
            System.exit(1);
        }
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port, 10, InetAddress.getByName("127.0.0.1"))) {
            log.info("En attente d'une connexion...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Connexion établie par l'IP : " + clientSocket.getLocalAddress());

                // à chaque connexion d'un client, on créé un thread qui va gérer la requête
                ClientHandler clientHandler = new ClientHandler(clientSocket, webroot);
                new Thread(clientHandler).start();
            }
        }
    }

    public static void main(String[] args) {
        parseProperties();
        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
