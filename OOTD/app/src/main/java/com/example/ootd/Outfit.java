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

    public Outfit(List<Garment> createdOutfit) {
        //this.outfitName = outfitName;
        this.favorites = false;
        this.createdOutfit = createdOutfit;
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
