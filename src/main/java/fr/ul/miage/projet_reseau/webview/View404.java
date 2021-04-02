package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View404 extends View implements ErrorPage {

    public View404(DataOutputStream dos) {
        super("HTTP/1.1 404 Not Found", ErrorPage.contentType, dos);
    }

    @Override
    public void sendResponse(String host) throws IOException {
        //TODO
    }
}
