package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public abstract class ViewError extends View {

    protected ViewError(String httpCode, DataOutputStream dos) {
        super(httpCode, dos);
    }

    @Override
    public void sendResponse(String pathToFile) throws IOException {
        File file = new File("error-pages/" + pathToFile);
        try (FileInputStream fis = new FileInputStream(file)) {
            dos.writeBytes(getHttpCode());
            dos.writeBytes("Content-Type: text/html");
            dos.writeBytes(String.format("Content-Length: %d%n", fis.available()));
            dos.writeBytes("\r\n");
            dos.write(fis.readAllBytes());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
