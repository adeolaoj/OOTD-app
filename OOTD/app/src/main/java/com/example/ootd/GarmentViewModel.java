package com.example.ootd;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GarmentViewModel extends ViewModel {
    private MutableLiveData<List<Garment>> garmentsData = new MutableLiveData<>();
    private MutableLiveData<List<List<Garment>>> outfitsSaved = new MutableLiveData<>(new ArrayList<>());

    public GarmentViewModel() {
        fetchGarmentData(); // Fetch data initially or when the ViewModel is instantiated
    }

    public LiveData<List<Garment>> getGarmentsData() {
        return garmentsData;
    }

    public void setGarmentsData(List<Garment> list) {
        garmentsData.setValue(list);
    }

    public LiveData<List<List<Garment>>> getSavedOutfits() {
        return outfitsSaved;
    }

    public void saveOutfit(List<Garment> outfit) {
        List<List<Garment>> currentOutfits = outfitsSaved.getValue();
        if (currentOutfits == null) {
            currentOutfits = new ArrayList<>();
        }
        currentOutfits.add(new ArrayList<>(outfit));
        outfitsSaved.setValue(currentOutfits);
    }

    public void fetchGarmentData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Garments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Garment> garments = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Garment garment = snapshot.getValue(Garment.class);
                    if (garment != null) {
                        garments.add(garment);
                    }
                }
                garmentsData.setValue(garments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("GarmentViewModel", "loadGarments:onCancelled", databaseError.toException());
            }
        });
    }
}
