package fr.ul.miage.projet_reseau;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Properties;

@Slf4j
public class Server {

    protected static int port;
    private static final String PROPERTIES_FILENAME = "config.properties";
    private static String webroot;
    private static boolean listing;

    /**
     * Parsing du fichier de configuration .properties pour récupérer le port TCP et le répertoire racine des sites web
     */
    public static void parseProperties() {
        var properties = new Properties();
        var file = new File(PROPERTIES_FILENAME);
        log.info(file.getAbsolutePath());
        try (InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
            port = Integer.parseInt(properties.getProperty("port"));
            webroot = properties.getProperty("webroot");
            listing = properties.getProperty("listing").equals("true");
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

    /**
     * Lance le serveur à partir en utilisant les infos du fichier properties.
     */
    public void start() throws IOException {
        try (var serverSocket = new ServerSocket(port, 10, InetAddress.getByName("127.0.0.1"))) {
            log.info("En attente d'une connexion...");
            while (true) {
                var clientSocket = serverSocket.accept();
                log.info("Connexion établie par l'IP : " + clientSocket.getLocalAddress());

                // à chaque connexion d'un client, on crée un thread qui va gérer la requête
                var clientHandler = new ClientHandler(clientSocket, webroot, listing);
                new Thread(clientHandler).start();
            }
        }
    }

    public static void main(String[] args) {
        parseProperties();
        var server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
