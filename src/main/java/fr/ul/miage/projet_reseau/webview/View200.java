package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public class View200 extends View {

    public View200(DataOutputStream dos) {
        super("HTTP/1.1 200 OK", dos);
    }

    @Override
    public void sendResponse(String location, String pathToFile) throws IOException {
        super.sendResponse(location, pathToFile);
    }
}
