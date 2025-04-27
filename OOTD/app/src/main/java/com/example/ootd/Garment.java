package com.example.ootd;

import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
import java.util.List;

public class Garment {
    private String Category;
    private String ImagePath;
    private String Subcategory;
    private List<String> ColorTags;
    private Boolean favorites = false;
    private String key;

    public Garment() {
        // private constructor for firestore
    }

    public Garment(String Category, String ImagePath, String Subcategory, List<String> colorTags) {
        this.Category = Category;
        this.ImagePath = ImagePath;
        this.Subcategory = Subcategory;
        this.ColorTags = colorTags;
        this.favorites = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImagePath() {
        if (ImagePath == null) {
            return "IMG_3234.jpeg";
        }
        return ImagePath;
    }

    public void setImagePath(String path) {
        this.ImagePath = path;
    }

    public String getCategory(){return Category;}
    public String getSubcategory(){return Subcategory;}

    public List<String> getGarmentTags() {return Arrays.asList(Category, Subcategory);}

    public List<String> getColorTags() { return ColorTags; }
    public Boolean isFavorite(){
        return favorites != null && favorites;
    }
    public void setFavorites(){
        favorites = !favorites;
    }
}
