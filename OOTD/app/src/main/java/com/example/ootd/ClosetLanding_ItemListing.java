package com.example.ootd;

import android.content.Context;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    private List<Garment> garmentList;

    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters

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
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public class GarmentAdapter extends RecyclerView.Adapter<GarmentAdapter.ViewHolder> {
        private List<Garment> garmentList;
        private Context context;

        public GarmentAdapter(List<Garment> dataSet, Context context) {
            this.garmentList = dataSet;
            this.context = context;
        }

        public void updateGarmentData(List<Garment> newData) {
            this.garmentList = newData;
            notifyDataSetChanged();  // Notifying the adapter to re-render the data
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.garment_layout, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Garment garment = garmentList.get(position);
            ChipGroup chipGroup = viewHolder.chipGroup;
            chipGroup.removeAllViews();

            String imagePath = garment.getImagePath();
            ImageView image = viewHolder.imageView;
            if (imagePath != null && !imagePath.isEmpty()) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child(imagePath);

                // Get the download URL and load it with Glide
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(viewHolder.imageView.getContext())
                                .load(uri)
                                .placeholder(R.drawable.garment_picture_default) // Show default while loading
                                .error(R.drawable.garment_picture_default) // Show default on error
                                .into(viewHolder.getImageView());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Error loading image", e);
                        // Optionally handle errors, e.g., by showing a default image or a toast
                        viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);
                    }
                });
            } else {
                // If no image path is available, use the default image
                viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);
            }


            if (garment.getGarmentTags() != null) {
                for (String tag : garment.getGarmentTags()) {
                    Chip chip = new Chip(context);
                    chip.setText(tag);
                    chip.setCloseIconVisible(false);
                    chipGroup.addView(chip);
                }
            }


            ImageButton favoriteBtn = viewHolder.favorite;
            favoriteBtn.setImageResource(garment.isFavorite() ? R.drawable.favorites_filled : R.drawable.favorites_unfilled);
            favoriteBtn.setOnClickListener(v -> {
                boolean isFavorite = garment.isFavorite();
                garment.setFavorites();
                favoriteBtn.setImageResource(!isFavorite ? R.drawable.favorites_filled : R.drawable.favorites_unfilled);
            });
        }

        @Override
        public int getItemCount() {
            return garmentList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;
            private final ChipGroup chipGroup;
            private final ImageButton favorite;

            public ViewHolder(View view) {
                super(view);
                chipGroup = view.findViewById(R.id.tagsList);
                imageView = view.findViewById(R.id.garmentImageView);
                favorite = view.findViewById(R.id.favorites);
            }

            public ImageView getImageView() {
                return imageView;
            }
        }

    }


}