package com.vyas.pranav.studentcompanion.data.models;

public class DownloadModel {

    private String name, extra_info, url;

    public DownloadModel(String name, String extra_info, String url) {
        this.name = name;
        this.extra_info = extra_info;
        this.url = url;
    }

    public DownloadModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
