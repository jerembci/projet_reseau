package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public class View401 extends View {

    public View401(DataOutputStream dos) {
        super("HTTP/1.1 401 Unauthorized", dos);
    }

    /**
     * Redéfinit la requête pour renvoyer un problème d'authentificaiton.
     * @throws IOException
     */
    public void sendResponse() throws IOException {
        dos.writeBytes(getHttpCode());
        dos.writeBytes("Content-Type: text/html\r\n");
        dos.writeBytes("WWW-Authenticate: Basic realm=\"Accès au site\", charset=\"UTF-8\"");
        dos.writeBytes("Content-length : \"\"");
        dos.writeBytes("\r\n");
    }
}
