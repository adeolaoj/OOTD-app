package com.example.ootd;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ootd.databinding.FragmentGarmentListingBinding;
import com.example.ootd.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


public class GarmentListingFragment extends Fragment {

    private FragmentGarmentListingBinding binding;
    Button saveButton;
    FirebaseStorage storage;
    StorageReference sref;
    FirebaseDatabase database;
    DatabaseReference dbref;


    public GarmentListingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGarmentListingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView image = binding.ListingImage;

        Bundle bundles = getArguments();
        String path = bundles.getString("ImagePath");
        setupDropdownMenu_Category();
        setupDropdownMenu_SubCategory();
        storage = FirebaseStorage.getInstance();
        sref = storage.getReference();
        StorageReference imageRef = sref.child(path);

        saveButton = binding.SaveListing;

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference("Garments");

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL
                Glide.with(getView())
                        .load(uri.toString())
                        .into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Error getting data", e);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = binding.Tops.getText().toString();
                String subcategory = binding.Blouse.getText().toString();
                if (category.isEmpty()) {
                    Toast.makeText(getActivity(), "Category Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (subcategory.isEmpty()) {
                    Toast.makeText(getActivity(),"Subcategory Required",Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = dbref.push().getKey();
                Map<String, Object> userData = new HashMap<>();

                userData.put("ImagePath", path);
                userData.put("Category", category);
                userData.put("Subcategory", subcategory);

                dbref.child(userId).setValue(userData);

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_closet);
            }
        });

        return root;
    }

    public void setupDropdownMenu_Category() {
        String[] items = getResources().getStringArray(R.array.category_dropdown_menu_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, items);

        MaterialAutoCompleteTextView autoCompleteTextView = binding.Tops;
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            view.setBackgroundColor(getResources().getColor(R.color.highlight_grey));
            autoCompleteTextView.setText(adapter.getItem(i));
        });
    }
    public void setupDropdownMenu_SubCategory() {
        String[] items = getResources().getStringArray(R.array.subcategory_dropdown_menu_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, items);

        MaterialAutoCompleteTextView autoCompleteTextView = binding.Blouse;
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            view.setBackgroundColor(getResources().getColor(R.color.highlight_grey));
            autoCompleteTextView.setText(adapter.getItem(i));
        });
    }

}