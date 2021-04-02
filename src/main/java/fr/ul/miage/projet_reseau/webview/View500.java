package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View500 extends View implements ErrorPage {

    public View500(DataOutputStream dos) {
        super("HTTP/1.1 500 Internal Server Error", ErrorPage.contentType, dos);
    }

    @Override
    public void sendResponse(String host) throws IOException {
        //TODO
    }
}
