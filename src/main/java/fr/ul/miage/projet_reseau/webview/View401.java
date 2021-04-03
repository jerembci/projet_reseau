package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View401 extends ViewError implements ErrorPage {

    public View401(DataOutputStream dos) {
        super("HTTP/1.1 401 Unauthorized", ErrorPage.contentType, dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse("401.html");
    }
}
