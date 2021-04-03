package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public class View404 extends ViewError implements ErrorPage {

    public View404(DataOutputStream dos) {
        super("HTTP/1.1 404 Not Found", ErrorPage.contentType, dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse("404.html");
    }
}
