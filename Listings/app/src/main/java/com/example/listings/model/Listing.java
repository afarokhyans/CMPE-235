package com.example.listings.model;

public class Listing {
    private String ListingId;
    private String Name;
    private String Summary;
    private String PictureUrl;
    private String HostPictureUrl;
    private String Price;
    private int NumberOfReviews;
    private float ReviewScoresRating;
    private String Description;
    private int Bedrooms;
    private int Bathrooms;
    private int Accommodates;


    public Listing() {

    }

    public String getListingId() {
        return ListingId;
    }

    public void setListingId(String listingId) {
        ListingId = listingId;
    }

    public String getHostPictureUrl() {
        return HostPictureUrl;
    }

    public void setHostPictureUrl(String hostUrl) {
        HostPictureUrl = hostUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public float getReviewScoresRating() { ReviewScoresRating = (ReviewScoresRating * 5)/100; return ReviewScoresRating; }

    public void setReviewScoresRating(float rating) {
        ReviewScoresRating = (rating * 5)/100;
    }

    public int getNumberofReviews() { return NumberOfReviews;}

    public void setNumberOfReviews(int numberOfReviews) {
        NumberOfReviews = numberOfReviews;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getBedrooms() { return Bedrooms; }

    public void setBedrooms(int bedrooms) { Bedrooms = bedrooms; }

    public int getBathrooms() { return Bathrooms; }

    public void setBathrooms(int bathrooms) { Bathrooms = bathrooms; }

    public int getAccommodates() { return Accommodates; }

    public void setAccommodates(int accommodates) { Accommodates = accommodates; }
}

