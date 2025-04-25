package com.example.ootd;

import java.util.Arrays;
import java.util.List;

public class Outfit {
    private String outfitName;
    private Boolean favorites = false;
    private List<Garment> createdOutfit;

    public Outfit() {
        // private constructor for firestore
    }

    public Outfit(String outfitName, List<Garment> createdOutfit) {
        this.outfitName = outfitName;
        this.createdOutfit = createdOutfit;
        this.favorites = false;
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

}
