package com.example.tsvetelinastoyanova.b3_listsintro;

import android.os.Parcel;
import android.os.Parcelable;

public class Planet implements Parcelable {
    private String name;
    private String description;
    private int pictureId;
    private String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(pictureId);
        dest.writeString(url);
    }

    public static final Creator<Planet> CREATOR = new Creator<Planet>() {
        @Override
        public Planet createFromParcel(Parcel in) {
            return new Planet(in);
        }

        @Override
        public Planet[] newArray(int size) {
            return new Planet[size];
        }
    };

    protected Planet(Parcel in) {
        name = in.readString();
        description = in.readString();
        pictureId = in.readInt();
        url = in.readString();
    }

    public Planet(String name, String description, int pictureId, String url) {
        this.name = name;
        this.description = description;
        this.pictureId = pictureId;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}