package com.example.hamza.markan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

class Comment implements Parcelable {
    private String userId;
    private String storeId;
    private String title;
    private String comment;
    private float rating;
    private GeoPoint location;
    private String date;

    public Comment(String userId, String storeId, String title, String comment, float rating, GeoPoint location, String date){
        this.userId = userId;
        this.storeId = storeId;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
        this.location = location;
        this.date = date;
    }

    protected Comment(Parcel in) {
        userId = in.readString();
        storeId = in.readString();
        title = in.readString();
        comment = in.readString();
        rating = in.readFloat();
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        location = new GeoPoint(lat, lng);
        date = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUserId(){
        return userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public double getRating() {
        return rating;
    }

    public GeoPoint getLocation() { return location; }

    public String getDate() { return  date; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(storeId);
        parcel.writeString(title);
        parcel.writeString(comment);
        parcel.writeFloat(rating);
        parcel.writeDouble(location.getLatitude());
        parcel.writeDouble(location.getLongitude());
        parcel.writeString(date);
    }
}
