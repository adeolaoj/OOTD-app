package com.example.ootd;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;

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
import java.util.HashMap;
import java.util.List;

public class GarmentViewModel extends ViewModel {
    private MutableLiveData<List<Garment>> garmentsData = new MutableLiveData<>();
    private MutableLiveData<List<Outfit>> outfitsSaved = new MutableLiveData<>(new ArrayList<>());
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

    public MutableLiveData<List<Outfit>> getSavedOutfits() {
        fetchOutfits();
        return outfitsSaved;
    }

    public void fetchOutfits() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String username = task.getResult().getValue(String.class);

                if (username != null) {
                    DatabaseReference outfitsRef = FirebaseDatabase.getInstance()
                            .getReference("data")
                            .child(username)
                            .child("outfits");

                    outfitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Outfit> outfitsList = new ArrayList<>();

                            for (DataSnapshot outfitSnapshot : snapshot.getChildren()) {
                                List<Garment> garmentsInOutfit = new ArrayList<>();

                                for (DataSnapshot garmentSnapshot : outfitSnapshot.getChildren()) {
                                    String imagePath = garmentSnapshot.getValue(String.class);

                                    if (imagePath != null) {
                                        Garment garment = new Garment();
                                        garment.setImagePath(imagePath);
                                        garmentsInOutfit.add(garment);
                                    }
                                }



                                Outfit outfit = new Outfit(garmentsInOutfit);
                                outfit.setName(outfitSnapshot.getKey());
                                outfitsList.add(outfit);
                            }

                            outfitsSaved.setValue(outfitsList); // ✅ Update LiveData
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("GarmentViewModel", "Failed to fetch outfits", error.toException());
                        }
                    });

                } else {
                    Log.e("GarmentViewModel", "Username is null");
                }
            } else {
                Log.e("GarmentViewModel", "Failed to fetch username");
            }
        });
    }


    public void saveOutfit(Outfit outfit) {
        List<Outfit> currentOutfits = outfitsSaved.getValue();
        if (currentOutfits == null) {
            currentOutfits = new ArrayList<>();
        }
        currentOutfits.add(outfit);
        outfitsSaved.setValue(currentOutfits);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String username = task.getResult().getValue(String.class);

                if (username != null) {
                    String name = outfit.getOutfitName();
                    if (name == null || name.isEmpty()) {
                        Log.e("GarmentViewModel", "Outfit name is null or empty!");
                        return;
                    }

                    DatabaseReference outfitsRef = FirebaseDatabase.getInstance()
                            .getReference("data")
                            .child(username)
                            .child("outfits")
                            .child(name);

                    ArrayList<String> garmentList = new ArrayList<>();
                    for (Garment garment : outfit.getOutfitGarments()) {
                        garmentList.add(garment.getImagePath());
                    }

                    outfitsRef.setValue(garmentList);
                } else {
                    Log.e("GarmentViewModel", "Username is null");
                }
            } else {
                Log.e("GarmentViewModel", "Failed to fetch username");
            }
        });
    }




    public void fetchGarmentData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getUid();

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("users").child(user);

        user_ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                username = task.getResult().getValue(String.class);
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
                            garment.setKey(snapshot.getKey());
                            garments.add(garment);
                        }
                    }
                    garmentsData.setValue(garments);

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
