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

    public void sendHTML(String html) throws IOException {
        dos.writeBytes(getHttpCode() + "\r\n");
        dos.writeBytes("Content-Type: text/html\r\n");
        dos.writeBytes(String.format("Content-Length: %d%n", html.getBytes().length));
        dos.writeBytes("\r\n");
        dos.writeBytes(html);
    }
}
