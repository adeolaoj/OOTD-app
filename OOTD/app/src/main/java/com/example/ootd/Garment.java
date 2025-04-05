package com.example.ootd;

import java.util.List;

public class Garment {
    private Integer imageAddress;
    private List<String> garmentTags; // NOTE: REPLACE WITH FIREBASE DATA STRUCTURE

    public Garment(Integer imageAddress, List<String> garmentTags) {
        this.imageAddress = imageAddress;
        this.garmentTags = garmentTags;
    }

    public Integer getImageAddress() {return imageAddress;}
    public List<String> getGarmentTags(){return garmentTags;}
}
