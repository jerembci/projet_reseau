package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View400 extends View {

    public View400(DataOutputStream dos) {
        super("HTTP/1.1 400 Bad Request", dos);
    }

    public void sendResponse(String webroot) throws IOException {
        super.sendResponse(webroot + LOCATION_ERRORS, ErrorPage.PAGE400.page());
    }
}
