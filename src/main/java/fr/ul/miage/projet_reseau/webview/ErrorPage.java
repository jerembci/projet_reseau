package fr.ul.miage.projet_reseau.webview;

public enum ErrorPage {
    PAGE400("400.html"),
    PAGE401("401.html"),
    PAGE403("403.html"),
    PAGE404("404.html"),
    PAGE500("500.html");

    private final String page;

    ErrorPage(String page) {
        this.page = page;
    }

    public String page() {
        return page;
    }
}
