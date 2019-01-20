package com.vyas.pranav.studentcompanion.data.itemdatabase.firebase;

public class ItemModel {
    String name, p_name, contact, category, extra_info, image_uri;
    float price;

    public ItemModel() {
    }

    public ItemModel(String name, String p_name, String contact, String category, String extra_info, String image_uri, float price) {
        this.name = name;
        this.p_name = p_name;
        this.contact = contact;
        this.category = category;
        this.extra_info = extra_info;
        this.image_uri = image_uri;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
