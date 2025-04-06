package com.example.ootd;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClosetLanding_ItemListing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClosetLanding_ItemListing extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClosetLanding_ItemListing() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClosetLanding_ItemListing.
     */
    // TODO: Rename and change types and number of parameters
    public static ClosetLanding_ItemListing newInstance(String param1, String param2) {
        ClosetLanding_ItemListing fragment = new ClosetLanding_ItemListing();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_closet_landing__item_listing, container, false);

        recyclerView = view.findViewById(R.id.garmentRecyclerView);
        recyclerView.setPadding(0, 0, 0, 160);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        GarmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(GarmentViewModel.class);

        GarmentAdapter adapter = new GarmentAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        // set up the data, get the data from the ViewModel
        viewModel.getGarmentsData().observe(getViewLifecycleOwner(), garments ->{
            adapter.updateGarmentData(garments);
        });


        // Inflate the layout for this fragment
        return view;
    }

    public static class GarmentAdapter extends RecyclerView.Adapter<GarmentAdapter.ViewHolder> {

        private List<Garment> garmentList;
        private Context context;

        /**
         * Initialize the dataset of the Adapter
         */

        public GarmentAdapter(List<Garment> dataSet, Context context) {
            this.garmentList = dataSet;
            this.context = context;
        }

        public void updateGarmentData(List<Garment> list) {
            this.garmentList = list;
            notifyDataSetChanged();
        }


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            //private final TextView textView;
            private final ImageView imageView;
            private final ChipGroup chipGroup;
            private final ImageButton favorite;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                chipGroup = view.findViewById(R.id.tagsList);
                imageView = view.findViewById(R.id.garmentImageView);
                favorite = view.findViewById(R.id.favorites);
            }

            public ChipGroup getChips() {
                return chipGroup;
            }

            public ImageView getImageView() {
                return imageView;
            }

            public ImageButton getFavoriteButton() {
                return favorite;
            }
        }



        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.garment_layout, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            Garment currGarment = garmentList.get(position);

            ChipGroup chipGroup = viewHolder.chipGroup;

            chipGroup.removeAllViews();
            for (String tag : currGarment.getGarmentTags()) {
                Chip chip = new Chip(context);
                chip.setText(tag);
                chip.setCloseIconVisible(false);
                chipGroup.addView(chip);
            }

            ImageButton favoriteBtn = viewHolder.favorite;

            if (currGarment.isFavorite()) {
                favoriteBtn.setImageResource(R.drawable.favorites_filled);
            }

            
            favoriteBtn.setOnClickListener(v -> {
                garmentList.get(viewHolder.getAdapterPosition()).setFavorites();

                if (garmentList.get(viewHolder.getAdapterPosition()).isFavorite()) {
                    favoriteBtn.setImageResource(R.drawable.favorites_filled);
                } else {
                    favoriteBtn.setImageResource(R.drawable.favorites_unfilled);
                }
            });

            // Get element from dataset at the corresponding positions and replace the
            // contents of the view with a picture of the garment
            //TODO: REPLACE WITH NON-PLACEHOLDER IMAGE
            viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return garmentList.size();
        }
    }

}