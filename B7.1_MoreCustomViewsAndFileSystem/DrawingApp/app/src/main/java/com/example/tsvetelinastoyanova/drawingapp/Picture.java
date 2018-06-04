package com.example.tsvetelinastoyanova.drawingapp;

public class Picture {
    String name;
    String lastModified;
    private int imageUrl;

    public Picture(String name, String lastModified, int imageUrl) {
        this.name = name;
        this.lastModified = lastModified;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLastModified() {
        return lastModified;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
