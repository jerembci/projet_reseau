package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View401 extends ViewError {

    public View401(DataOutputStream dos) {
        super("HTTP/1.1 401 Unauthorized", dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse(ErrorPage.PAGE401.page());
    }
}
