package fr.ul.miage.projet_reseau.webview;

import java.io.DataOutputStream;
import java.io.IOException;

public class View403 extends View {

    public View403(DataOutputStream dos) {
        super("HTTP/1.1 403 Forbidden Access", dos);
    }

    public void sendResponse() throws IOException {
        super.sendResponse(LOCATION_ERRORS, ErrorPage.PAGE403.page());
    }
}
