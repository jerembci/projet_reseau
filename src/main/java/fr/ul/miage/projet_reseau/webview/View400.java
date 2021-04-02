package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View400 extends View implements ErrorPage {

    public View400(DataOutputStream dos) {
        super("HTTP/1.1 400 Bad Request", ErrorPage.contentType, dos);
    }

    @Override
    public void sendResponse(String host) throws IOException {
        //TODO
    }
}
