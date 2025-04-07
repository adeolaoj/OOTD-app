package com.example.ootd;

import java.util.Arrays;
import java.util.List;

public class Garment {
    private Integer ImagePath;
    private String category;
    private String subcategory;
    private List<String> colorTags;
    private Boolean favorites;



    public Garment() {
        // private constructor for firestore
    }



    //public Garment(Integer imageAddress, List<String> garmentTags, List<String> colorTags) {
    public Garment(Integer imageAddress, String category, String subcategory, List<String> colorTags) {
        this.ImagePath = imageAddress;
        this.category = category;
        this.subcategory = subcategory;
        this.colorTags = colorTags;
        this.favorites = false;
    }



    public Integer getImageAddress() {return ImagePath;}
    public String getCategory(){return category;}
    public String getSubcategory(){return subcategory;}

    public List<String> getGarmentTags() {return Arrays.asList(category, subcategory);}

    public List<String> getColorTags() { return colorTags; }
    public Boolean isFavorite(){
        return favorites != null && favorites;
    }
    public void setFavorites(){
        favorites = !favorites;
    }
}
