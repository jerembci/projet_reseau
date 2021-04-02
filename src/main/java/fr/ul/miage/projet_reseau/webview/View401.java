package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View401 extends View implements ErrorPage {

    public View401(DataOutputStream dos) {
        super("HTTP/1.1 401 Unauthorized", ErrorPage.contentType, dos);
    }

    @Override
    public void sendResponse(String host) throws IOException {
        //TODO
    }
}
