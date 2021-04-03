package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View400 extends ViewError {

    public View400(DataOutputStream dos) {
        super("HTTP/1.1 400 Bad Request", dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse(ErrorPage.PAGE400.page());
    }
}
