package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View500 extends View {

    public View500(DataOutputStream dos) {
        super("HTTP/1.1 500 Internal Server Error", dos);
    }

    public void sendResponse(String webroot) throws IOException {
        super.sendResponse(webroot + LOCATION_ERRORS, ErrorPage.PAGE500.page());
    }
}
