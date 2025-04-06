package com.example.ootd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ootd.databinding.FragmentGarmentListingBinding;
import com.example.ootd.databinding.FragmentProfileBinding;

public class GarmentListingFragment extends Fragment {

    private FragmentGarmentListingBinding binding;

    public GarmentListingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGarmentListingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}