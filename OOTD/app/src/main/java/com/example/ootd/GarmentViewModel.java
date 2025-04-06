package com.example.ootd;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
    Class that contains information about the Garments. Essentially a list of garments
    that automatically updates in all views/fragments (closet, add/plan, etc.)
    Can be updated to reference database once we get that connected.
 */

public class GarmentViewModel extends ViewModel {
    private MutableLiveData<List<Garment>> garmentsData = new MutableLiveData<>();
    //private FirebaseFirestore database = FirebaseFirestore.getInstance();

    public LiveData<List<Garment>> getGarmentsData() {
        return garmentsData;
    }

    public void setGarmentsData(List<Garment> list) {
        garmentsData.setValue(list);
    }


    /*
    public void fetchGarmentData() {
        database.collection("Garments").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                List<Garment> garmentList = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    String id = snapshot.getId();
                    Long imageAddress = snapshot.getLong("imageAddress");

                    //Garment piece = new Garment(imageAddress, id);
                    //garmentList.add(piece);
                }

                garmentsData.setValue(garmentList);
            }
        });
    }
     */

}
