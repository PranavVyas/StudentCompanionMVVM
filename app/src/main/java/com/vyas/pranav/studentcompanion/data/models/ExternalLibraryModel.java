package com.vyas.pranav.studentcompanion.data.models;

public class ExternalLibraryModel {
    private String name, license, url;

    public ExternalLibraryModel(String name, String license, String url) {
        this.name = name;
        this.license = license;
        this.url = url;
    }

    public ExternalLibraryModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
