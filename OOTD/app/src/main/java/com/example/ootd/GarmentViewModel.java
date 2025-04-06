package com.example.ootd;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/*
    Class that contains information about the Garments. Essentially a list of garments
    that automatically updates in all views/fragments (closet, add/plan, etc.)
    Can be updated to reference database once we get that connected.
 */

public class GarmentViewModel extends ViewModel {
    private MutableLiveData<List<Garment>> garmentsData = new MutableLiveData<>();

    public LiveData<List<Garment>> getGarmentsData() {
        return garmentsData;
    }

    public void setGarmentsData(List<Garment> list) {
        garmentsData.setValue(list);
    }
}
