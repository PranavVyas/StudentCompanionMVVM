package com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore;

public class NotificationFirestoreModel {

    private String name, date, short_info, image_url;
    private String url = "";
    private String venue = "";

    public NotificationFirestoreModel() {
    }

    public NotificationFirestoreModel(String name, String date, String short_info, String image_url, String url, String venue) {
        this.name = name;
        this.date = date;
        this.short_info = short_info;
        this.image_url = image_url;
        this.url = url;
        this.venue = venue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShort_info() {
        return short_info;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
