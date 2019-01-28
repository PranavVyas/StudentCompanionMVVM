package com.vyas.pranav.studentcompanion.data;

public class LibraryChildModel {

    private String license, url;

    public LibraryChildModel(String license, String url) {
        this.license = license;
        this.url = url;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
