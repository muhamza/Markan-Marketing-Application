package com.example.hamza.markan;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class Store implements Parcelable{
    private String id;
    private String storeName;
    private String category;
    private String details;
    private GeoPoint coordinates;
    private String image;
    private String logo;
    private String tagline;

    public Store(String id, String storeName, String category, String details, GeoPoint coordinates, String image, String logo, String tagline) {
        this.id = id;
        this.storeName = storeName;
        this.category = category;
        this.details = details;
        this.coordinates = coordinates;
        this.image = image;
        this.logo = logo;
        this.tagline = tagline;
    }

    protected Store(Parcel in) {
        id = in.readString();
        storeName = in.readString();
        category = in.readString();
        details = in.readString();
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        coordinates = new GeoPoint(lat, lng);
        image = in.readString();
        logo = in.readString();
        tagline = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public  String getId(){ return id; }

    public String getStoreName() {
        return storeName;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }

    public GeoPoint getCoordinates(){
        return coordinates;
    }

    public String getImage(){ return image; }

    public String getLogo(){
        return logo;
    }

    public String getTagline(){
        return tagline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(storeName);
        parcel.writeString(category);
        parcel.writeString(details);
        parcel.writeDouble(coordinates.getLatitude());
        parcel.writeDouble(coordinates.getLongitude());
        parcel.writeString(image);
        parcel.writeString(logo);
        parcel.writeString(tagline);
    }
}

