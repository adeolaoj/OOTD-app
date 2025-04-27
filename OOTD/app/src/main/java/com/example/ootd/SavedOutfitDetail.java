package com.example.ootd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedOutfitDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedOutfitDetail extends Fragment {


    public SavedOutfitDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment savedOutfitDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedOutfitDetail newInstance(String param1, String param2) {
        SavedOutfitDetail fragment = new SavedOutfitDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_outfit_detail, container, false);
    }
}