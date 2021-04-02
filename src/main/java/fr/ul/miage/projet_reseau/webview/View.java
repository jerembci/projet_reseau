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

    public String getHttpCode() {
        return httpCode;
    }

    public String getContentType() {
        return contentType;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }

    public abstract void sendResponse(String host) throws IOException;
}
