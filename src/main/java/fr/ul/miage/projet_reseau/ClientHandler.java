package fr.ul.miage.projet_reseau;

import fr.ul.miage.projet_reseau.webview.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final String webroot;
    private String auth = "";

    public ClientHandler(Socket socket) {
        this.socket = socket;
        // Valeur par défaut
        this.webroot = "sites/";
    }

    /**
     * On permet à l'utilisateur de renseigner la racine des sites avec un '/' ou non.
     * S'il ne rensiegne pas le '/', on l'ajoute automatiquement.
     * @param socket Socket.
     * @param webroot Racine des sites.
     */
    public ClientHandler(Socket socket, String webroot) {
        this.socket = socket;
        if (webroot.charAt(webroot.length() - 1) == '/') {
            this.webroot = webroot;
        } else {
            this.webroot = webroot + '/';
        }
    }

    /**
     * Lit la requête récupérée par le serveur et extrait les infos importantes.
     * On extrait la première ligne de la requête, l'hôte et les credentials pour faire une vérification dans la méthode parseRequest().
     */
    @Override
    public void run() {
        try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String request = in.readLine();
            String host = "";

            // Parsing du header
            while (in.ready()) {
                String line = in.readLine();
                log.info(line);
                if (line.toUpperCase().startsWith("HOST:")) {
                    host = line;
                }
                if (line.toUpperCase().startsWith("AUTHORIZATION:")) {
                    auth = line;
                }
            }

            log.info("Requête : " + request);

            String hostName = host.split(" ")[1];
            int dotIndex = hostName.indexOf(".");
            hostName = hostName.substring(0, dotIndex);

            log.info("Host : " + hostName);

            parseRequest(dos, request, hostName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse la requête récupérée par le serveur.
     * Plusieurs vues sont créées pour afficher les différentes infos suivant la situation dans laquelle on se trouve.
     * A chaque problème rencontré (mauvaise authentification, fichier inexistant, erreur de connexion), on renvoie
     * une page pour informer l'utilisateur du problème survenu.
     * @param dos
     * @param request
     * @param hostName
     * @throws IOException
     */
    private void parseRequest(DataOutputStream dos, String request, String hostName) throws IOException {
        if (request.startsWith("GET")) {
            String path = request.split(" ")[1];
            path = path.equals("/") ? "/index.html" : path;
            if (new File(webroot + hostName + path).isFile()) {
                // Le fichier existe, on continue l'exécution

                // On check si le path vers le .htpasswd est correct pour éviter les problèmes lors de l'instanciation de File.
                String pathToHtpasswd = (path.contains("/") ? path.substring(0, path.lastIndexOf('/')) : "") + "/.htpasswd";
                File authFile = new File(webroot + hostName + pathToHtpasswd);
                if ((authFile.exists())) {
                    if (!auth.equals("")) {
                        if (checkCredentials(auth, authFile)) {
                            new View200(dos).sendResponse(webroot, hostName + path);
                        } else {
                            new View403(dos).sendResponse();
                        }
                    } else {
                        new View401(dos).sendResponse();
                    }
                } else {
                    new View200(dos).sendResponse(webroot, hostName + path);
                }
            } else {
                // Le fichier n'existe pas, on renvoie une erreur 404
                new View404(dos).sendResponse();
            }
            dos.flush();
        } else if (request.startsWith("POST")) {
            dos.writeBytes("POST");
            log.info("REQUÊTE POST");
        } else {
            // On n'a pas recu de requete GET ni POST, on renvoie une erreur 400
            new View400(dos).sendResponse();
        }
    }

    /**
     * Parse le fichier des credentials autorisés.
     * Crée un objet Credential contenant le username et le password pour chaque credential (classe déclarée en fin de fichier).
     * Plus simple de tout stocker dans une liste pour tout comparer lorsque l'on veut autoriser ou non un utilisateur à accéder à une page.
     * @param authFile Fichier des credentials autorisés.
     * @return Le fichier transformé en liste.
     */
    private List<Credentials> parseAuthFile(File authFile) {
        List<Credentials> credentialsList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(authFile))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] credentials = line.split(":");
                credentialsList.add(new Credentials(credentials[0], credentials[1]));
            }
        } catch (FileNotFoundException e) {
            log.error("Le fichier .htpasswd n'existe pas, impossible d'accéder à la page.");
        } catch (IOException e) {
            log.error("Un problème est survenu lors de l'analyse du fichier .htpasswd.");
        }
        return credentialsList;
    }

    /**
     * Vérifie si les credentials renseignés par l'utilisateur sont corrects pour l'autoriser à accéder à la page voulue.
     * @param auth Credentials renseignés (encodés en Base 64).
     * @param authFile Fichier contenant les credentials autorisés.
     * @return True si l'utilisateur est autorisé à accéder à la page, false sinon.
     */
    private boolean checkCredentials(String auth, File authFile) {
        String encodedCredentials = auth.split(" ")[2];
        String decodedString = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] expectedCredentials = decodedString.split(":");
        if (expectedCredentials.length == 2) {
            String username = decodedString.split(":")[0];
            String password = decodedString.split(":")[1];
            List<Credentials> credentialsList = parseAuthFile(authFile);
            return credentialsList.stream().anyMatch(c -> c.getUsername().equals(username) && passwordsMatching(c.getPassword(), password));
        }
        return false;
    }

    /**
     * Permet de comparer le mot de passe en clair renseigné par l'utilisateur avec les mots de passe hashés en MD5
     * autorisés dans le fichier .htpasswd.
     * @param md5Password Mot de passe hashé en MD5.
     * @param plainPassword Mot de passe en clair.
     * @return True si les mots de passe correspondent, false sinon.
     */
    private boolean passwordsMatching(String md5Password, String plainPassword) {
        String plainPasswordToMD5;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainPassword.getBytes());
            byte[] digest = md.digest();
            plainPasswordToMD5 = new BigInteger(1, digest).toString(16);
            return plainPasswordToMD5.equals(md5Password) || plainPasswordToMD5.equals(md5Password.replaceAll("^0+", ""));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    /**
     * Classe pour stocker les credentials autorisés.
     * Cela permet une vérification plus simple lorsqu'on en a besoin.
     */
    @Getter
    static class Credentials {
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}