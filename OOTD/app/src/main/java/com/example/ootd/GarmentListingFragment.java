package com.example.ootd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.ootd.databinding.FragmentGarmentListingBinding;
import com.example.ootd.databinding.FragmentProfileBinding;
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
    FirebaseDatabase database;
    DatabaseReference myRef;


    public GarmentListingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGarmentListingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setupDropdownMenu_Category();
        setupDropdownMenu_SubCategory();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Garment");


        saveButton = binding.SaveListing;

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

                Bundle bundle = getArguments();

                String userId = myRef.push().getKey();
                Map<String, Object> userData = new HashMap<>();

                if (bundle != null) {
                    String URL = bundle.getString("ImageURL");
                    userData.put("URL", URL);
                }

                userData.put("Category", category);
                userData.put("Subcategory", subcategory);

                myRef.child(userId).setValue(userData);

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_closet);
            }
        });

        return root;
    }

    public void setupDropdownMenu_Category() {
        String[] items = new String[]{"Tops"};
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
        String[] items = new String[]{"Blouse"};
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