package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View500 extends ViewError implements ErrorPage {

    public View500(DataOutputStream dos) {
        super("HTTP/1.1 500 Internal Server Error", ErrorPage.contentType, dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse("500.html");
    }
}
