package com.example.sunillakkad.travelmate.model;


import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {

    private String title;
    private String thumbnailURL;
    private String id;
    private String publishDate;

    public VideoItem() {
    }

    protected VideoItem(Parcel in) {
        title = in.readString();
        thumbnailURL = in.readString();
        id = in.readString();
        publishDate = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(thumbnailURL);
        parcel.writeString(id);
        parcel.writeString(publishDate);
    }
}
