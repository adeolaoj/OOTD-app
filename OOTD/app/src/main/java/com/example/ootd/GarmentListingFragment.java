package com.example.ootd;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ootd.databinding.FragmentGarmentListingBinding;
import com.example.ootd.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GarmentListingFragment extends Fragment {

    private FragmentGarmentListingBinding binding;
    Button saveButton;
    FirebaseStorage storage;
    StorageReference sref;
    FirebaseDatabase database;
    DatabaseReference dbref;
    DatabaseReference user_ref;
    String path;
    String username;
    String currentCategory;


    public GarmentListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGarmentListingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView image = binding.ListingImage;
        setupDropdownMenu_Category();
        setupDropdownMenu_SubCategory(null);

        Bundle bundles = getArguments();

        String path;
        if (bundles != null && bundles.containsKey("ImagePath")) {
            path = bundles.getString("ImagePath");

            EditText Category = binding.Tops;
            EditText Subcategory = binding.Blouse;

            Category.setText(bundles.getString("Category"));
            Subcategory.setText(bundles.getString("Subcategory"));

            Log.e("GarmentListingFragment", "ImagePath Found");
        } else {
            // debugging
            Log.e("GarmentListingFragment", "Missing 'ImagePath' argument");
            path = null;
            Toast.makeText(getContext(), "Missing image path", Toast.LENGTH_SHORT).show();
        }

        ArrayList<String> colorTags = bundles.getStringArrayList("ColorTags");
        ChipGroup chips = binding.colorGroup;

        if (colorTags != null) {
            for (int i = 0; i < chips.getChildCount(); ++i) {
                View chipView = chips.getChildAt(i);
                if (chipView instanceof Chip) {
                    Chip chip = (Chip) chipView;
                    String chipText = chip.getText().toString().trim(); // Important: trim spaces
                    if (colorTags.contains(chipText)) {
                        chip.setChecked(true);
                    } else {
                        chip.setChecked(false);
                    }
                }
            }
        }



        saveButton = binding.SaveListing;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getUid();

        user_ref = FirebaseDatabase.getInstance().getReference("users");

        user_ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                username = task.getResult().child(user).getValue(String.class);
                Log.e("GarmentListingFragment", "Username: " + username);

                dbref = FirebaseDatabase.getInstance().getReference("data").child(username).child("garments");

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

                        ArrayList<String> colorsSelected = new ArrayList<>();
                        ChipGroup chips = binding.colorGroup;

                        for (int i = 0; i < chips.getChildCount(); ++i) {
                            View chip = chips.getChildAt(i);
                            if (chip instanceof Chip) {
                                Chip color = (Chip) chip;
                                if (color.isChecked()) {
                                    colorsSelected.add(color.getText().toString());
                                }
                            }
                        }


                        if (bundles != null && bundles.containsKey("key")) {
                            String key = bundles.getString("key");

                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("ImagePath", path);
                            updatedData.put("Category", category);
                            updatedData.put("Subcategory", subcategory);
                            updatedData.put("ColorTags", colorsSelected);

                            dbref.child(key).updateChildren(updatedData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getActivity(), "Garment updated!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
                                    });

                        } else {
                            String userId = dbref.push().getKey();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("ImagePath", path);
                            userData.put("Category", category);
                            userData.put("Subcategory", subcategory);
                            userData.put("ColorTags", colorsSelected);
                            userData.put("favorites", "false");

                            dbref.child(userId).setValue(userData);
                        }

                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                        navController.navigate(R.id.navigation_closet);
                    }
                });


            } else {
                Log.d("GarmentListingFragment", "Username not found!");
            }
        });

        storage = FirebaseStorage.getInstance();
        sref = storage.getReference();
        StorageReference imageRef = sref.child(path);

        saveButton = binding.SaveListing;

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
            currentCategory = adapter.getItem(i);
            autoCompleteTextView.setText(adapter.getItem(i));
            setupDropdownMenu_SubCategory(currentCategory);
        });

        // fixes clearing selection issue
        autoCompleteTextView.setOnClickListener(v -> {
            if (!autoCompleteTextView.getText().toString().isEmpty()) {
                autoCompleteTextView.setText("");
                currentCategory = "";
                binding.Blouse.setText("");
            }
        });
    }
    public void setupDropdownMenu_SubCategory(String category) {

        int subcategoryId = getSubcategory(category);
        if (subcategoryId == 0) {
            binding.Blouse.setAdapter(null);
            binding.Blouse.setText("");
            return;
        }

        String[] items = getResources().getStringArray(subcategoryId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, items);

        MaterialAutoCompleteTextView autoCompleteTextView = binding.Blouse;
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            view.setBackgroundColor(getResources().getColor(R.color.highlight_grey));
            autoCompleteTextView.setText(adapter.getItem(i));
        });

        // fixes clearing selection issue
        autoCompleteTextView.setOnClickListener(v -> {
            if (!autoCompleteTextView.getText().toString().isEmpty()) {
                autoCompleteTextView.setText("");
            }
        });
    }

    private int getSubcategory(String category) {
        if (category == null || category.isEmpty()) {
            return 0;
        }
        switch (category) {
            case "Tops":
                return R.array.subcategory_tops;
            case "Bottoms":
                return R.array.subcategory_bottoms;
            case "Shoes":
                return R.array.subcategory_shoes;
            case "Outerwear":
                return R.array.subcategory_outerwear;
            case "Dresses":
                return R.array.subcategory_dresses;
            case "Swim":
                return R.array.subcategory_swim;
            case "Accessories":
                return R.array.subcategory_accessories;
            case "Jewelry":
                return R.array.subcategory_jewelry;
            case "Bags":
                return R.array.subcategory_bags;
            case "Headwear":
                return R.array.subcategory_headwear;
            case "Other":
                return R.array.subcategory_other;
            default:
                return 0;
        }
    }
}