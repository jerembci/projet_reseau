package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public class View404 extends View {

    public View404(DataOutputStream dos) {
        super("HTTP/1.1 404 Not Found", dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse(LOCATION_ERRORS, ErrorPage.PAGE404.page());
    }
}
