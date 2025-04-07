package com.example.ootd;

import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
import java.util.List;

public class Garment {
    private String ImagePath;
    private String Category;
    private String Subcategory;
    private List<String> colorTags;
    private Boolean favorites;

    public Garment() {
        // private constructor for firestore
    }



    public Garment(String ImagePath, String category, String subcategory, List<String> colorTags) {
        this.ImagePath = ImagePath;
        this.Category = category;
        this.Subcategory = subcategory;
        this.colorTags = colorTags;
        this.favorites = false;
    }



    public String getImagePath() {
        if (ImagePath == null) {
            return "IMG_3234.jpeg";
        }
        return ImagePath;
    }
    public String getCategory(){return Category;}
    public String getSubcategory(){return Subcategory;}

    public List<String> getGarmentTags() {return Arrays.asList(Category, Subcategory);}

    public List<String> getColorTags() { return colorTags; }
    public Boolean isFavorite(){
        return favorites != null && favorites;
    }
    public void setFavorites(){
        favorites = !favorites;
    }
}
