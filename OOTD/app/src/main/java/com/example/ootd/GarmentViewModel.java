package com.example.ootd;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
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
    private String username;
    private DatabaseReference ref;


    public GarmentViewModel() {
        fetchGarmentData(); // Fetch data initially or when the ViewModel is instantiated
    }

    public LiveData<List<Garment>> getGarmentsData() {
        return garmentsData;
    }

    public void addGarment(Garment g) {
        List<Garment> current = garmentsData.getValue();
        assert current != null;
        current.add(g);
        garmentsData.setValue(current);
    }

    public void setGarmentsData(List<Garment> list) {
        garmentsData.setValue(list);
    }

    public LiveData<List<Outfit>> getSavedOutfits() {
        return outfitsSaved;
    }

    public void saveOutfit(Outfit outfits) {
        List<Outfit> currentOutfits = outfitsSaved.getValue();
        if (currentOutfits == null) {
            currentOutfits = new ArrayList<>();
        }
        currentOutfits.add(outfits);
        outfitsSaved.setValue(currentOutfits);
    }

    public void fetchGarmentData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getUid();

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("users");

        user_ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                username = task.getResult().child(user).getValue(String.class);
                Log.e("GarmentListingFragment", "Username: " + username);
                ref = FirebaseDatabase.getInstance().getReference("data").child(username).child("garments");
            } else {
                ref = FirebaseDatabase.getInstance().getReference("Garments");
                Log.d("GarmentListingFragment", "Username not found!");
            }

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
        });
    }

}
