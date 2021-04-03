package fr.ul.miage.projet_reseau.webview;

import java.io.*;

public abstract class View {

    protected final String httpCode, contentType;
    protected final DataOutputStream dos;

    protected View(String httpCode, DataOutputStream dos) {
        this.httpCode = httpCode;
        this.contentType = "";
        this.dos = dos;
    }

    protected View(String httpCode, String contentType, DataOutputStream dos) {
        this.httpCode = httpCode;
        this.contentType = contentType;
        this.dos = dos;
    }

    protected String retrieveContentType(String path) {
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

    public abstract void sendResponse(String pathToFile) throws IOException;

    public String getHttpCode() {
        return httpCode;
    }

    public String getContentType() {
        return contentType;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }
}
