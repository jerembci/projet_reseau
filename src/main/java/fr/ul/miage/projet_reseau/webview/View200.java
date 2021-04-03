package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public class View200 extends View {

    public View200(DataOutputStream dos) {
        super("HTTP/1.1 200 OK", dos);
    }

    @Override
    public void sendResponse(String pathToFile) throws IOException {
        File file = new File("sites/" + pathToFile);
        try (FileInputStream fis = new FileInputStream(file)) {
            dos.writeBytes(getHttpCode());
            dos.writeBytes(retrieveContentType(pathToFile));
            dos.writeBytes(String.format("Content-Length: %d%n", fis.available()));
            dos.writeBytes("\r\n");
            dos.write(fis.readAllBytes());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
