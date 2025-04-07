package com.example.ootd;

import java.util.List;

public class Garment {
    private Integer imageAddress;
    private List<String> garmentTags; // NOTE: REPLACE WITH FIREBASE DATA STRUCTURE
    private List<String> colorTags;
    private Boolean favorites;

    /*
    public Garment() {
        // private constructor for firestore
    }

     */

    //public Garment(Integer imageAddress, List<String> garmentTags, List<String> colorTags) {
    public Garment(Integer imageAddress, List<String> garmentTags) {
        this.imageAddress = imageAddress;
        this.garmentTags = garmentTags;
        //this.colorTags = colorTags;
        this.favorites = false;
    }

    public Integer getImageAddress() {return imageAddress;}
    public List<String> getGarmentTags(){return garmentTags;}
    //public List<String> getColorTags() { return colorTags; }
    public Boolean isFavorite(){
        return favorites;
    }
    public void setFavorites(){
        favorites = !favorites;
    }
}
