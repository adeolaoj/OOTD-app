package com.example.ootd;

import java.util.List;

public class Garment {
    private String imageAddress;
    private List<String> garmentTags; // NOTE: REPLACE WITH FIREBASE DATA STRUCTURE

    public Garment(String imageAddress, List<String> garmentTags) {
        this.imageAddress = imageAddress;
        this.garmentTags = garmentTags;
    }

    public String getImageAddress() {return imageAddress;}
    public List<String> getGarmentTags(){return garmentTags;}
}
