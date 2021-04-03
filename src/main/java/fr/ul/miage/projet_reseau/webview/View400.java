package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View400 extends ViewError implements ErrorPage {

    public View400(DataOutputStream dos) {
        super("HTTP/1.1 400 Bad Request", ErrorPage.contentType, dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse("400.html");
    }
}
