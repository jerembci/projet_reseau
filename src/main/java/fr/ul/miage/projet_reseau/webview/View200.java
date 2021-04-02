package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public class View200 extends View {

    private String path;

    public View200(String path, DataOutputStream dos) {
        super("HTTP/1.1 200 OK", dos);
        this.path = path;
    }

    @Override
    public void sendResponse(String host) throws IOException {
        File file = new File("sites/" + host + path);
        try (FileInputStream fis = new FileInputStream(file)) {
            dos.writeBytes(getHttpCode());
            dos.writeBytes(retrieveContentType());
            dos.writeBytes(String.format("Content-Length: %d%n", fis.available()));
            dos.writeBytes("\r\n");
            dos.write(fis.readAllBytes());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private String retrieveContentType() {
        String extension = path.substring(path.lastIndexOf("."));
        switch (extension) {
            case ".html":
            case ".htm":
                return "Content-Type: text/html\r\n";
            case ".css":
            case ".sass":
                return "Content-Type: text/css\r\n";
            case ".js":
            case ".min.js":
                return "Content-Type: application/javascript\r\n";
            case ".jpg":
            case ".jpeg":
                return "Content-Type: image/jpeg\r\n";
            case ".png":
                return "Content-Type: image/png\r\n";
            case ".gif":
                return "Content-Type: image/gif\r\n";
            default:
                return "Content-Type: \r\n";
        }
    }
}
