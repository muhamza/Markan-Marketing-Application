package com.example.hamza.markan;

class Comment {
    private String userId;
    private String storeId;
    private String title;
    private String comment;
    private float rating;

    public Comment(String userId, String storeId, String title, String comment, Float rating){
        this.userId = userId;
        this.storeId = storeId;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
    }

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
}
