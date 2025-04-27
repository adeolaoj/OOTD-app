package com.example.ootd;

import java.util.Arrays;
import java.util.List;

public class Outfit {
    private String outfitName;
    private Boolean favorites = false;
    private List<Garment> createdOutfit;
    private String key;

    public Outfit() {
        // private constructor for firestore
    }

    public Outfit(String outfitName, List<Garment> createdOutfit) {
        this.outfitName = outfitName;
        this.favorites = false;
        this.createdOutfit = createdOutfit;
    }

    public void setName(String name) {
        this.outfitName = name;
    }

    public Boolean isFavorite(){
        return favorites != null && favorites;
    }
    public void setFavorites(){
        favorites = !favorites;
    }

    public String getOutfitName() {
        return outfitName;
    }

    public Boolean isEmpty() {
        return createdOutfit.isEmpty();
    }

    public List<Garment> getOutfitGarments() {
        return createdOutfit;
    }

    public Garment getGarment(Garment garment) {
        return garment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
