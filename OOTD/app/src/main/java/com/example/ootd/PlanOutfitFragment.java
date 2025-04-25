package com.example.ootd;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanOutfitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanOutfitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button donePlanningBtn;

    public PlanOutfitFragment() {
        // Required empty public constructor
    }

    public static PlanOutfitFragment newInstance(String param1, String param2) {
        PlanOutfitFragment fragment = new PlanOutfitFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan_outfit, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.planOutfitRecyclerView);
        recyclerView.setPadding(0, 0, 0, 160);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        GarmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(GarmentViewModel.class);

        SelectedGarmentsViewModel garmentsForOutfit = new ViewModelProvider(requireActivity()).get(SelectedGarmentsViewModel.class);

        PlanOutfitAdapter adapter = new PlanOutfitAdapter(new ArrayList<>(), getContext(), garmentsForOutfit);
        recyclerView.setAdapter(adapter);

        viewModel.getGarmentsData().observe(getViewLifecycleOwner(), garments ->{
            adapter.updateGarmentData(garments);
        });

        Button donePlanning = view.findViewById(R.id.done_planning_button);



        return view;
    }

    public static class PlanOutfitAdapter extends RecyclerView.Adapter<PlanOutfitAdapter.ViewHolder> {

        private List<Garment> garmentList;
        private Context context;
        private SelectedGarmentsViewModel selectedGarments;

        public PlanOutfitAdapter(List<Garment> dataSet, Context context, SelectedGarmentsViewModel selectedGarments) {
            this.garmentList = dataSet;
            this.context = context;
            this.selectedGarments = selectedGarments;
        }

        public void updateGarmentData(List<Garment> list) {
            this.garmentList = list;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            //private final TextView textView;
            private final ImageView imageView;
            private final ChipGroup chipGroup;
            private final ImageButton favorite;
            private final CardView garmentCard;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                chipGroup = view.findViewById(R.id.tagsList);
                imageView = view.findViewById(R.id.garmentImageView);
                favorite = view.findViewById(R.id.favorites);
                garmentCard = view.findViewById(R.id.garmentCard);
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
        public PlanOutfitAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.garment_layout, viewGroup, false);

            return new PlanOutfitAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(PlanOutfitAdapter.ViewHolder viewHolder, final int position) {

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

            // Get element from dataset at the corresponding positions and replace the
            // contents of the view with a picture of the garment

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child(currGarment.getImagePath()).getDownloadUrl().addOnSuccessListener( uri->{
                Glide.with(context).load(uri).into(viewHolder.getImageView());
            }).addOnFailureListener(e-> {
                Log.e("ImageLoadError", "Could not load image: " + e.getMessage());
                viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);
            });

            CardView garmentSelected = viewHolder.garmentCard;
            selectedGarments.clearSelection();

            garmentSelected.setOnClickListener(v->{
                if (selectedGarments.contains(currGarment)) {
                    selectedGarments.removeGarment(currGarment);
                    garmentSelected.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
                } else if (selectedGarments.numGarments() < 5) {
                    selectedGarments.addGarment(currGarment);
                    garmentSelected.setCardBackgroundColor(ContextCompat.getColor(context, R.color.highlight_grey));
                } else {
                    Toast toast = Toast.makeText(context, "You may only select up to 5 garments", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return garmentList.size();
        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        donePlanningBtn = view.findViewById(R.id.done_planning_button);
        SelectedGarmentsViewModel garmentsForOutfit = new ViewModelProvider(requireActivity()).get(SelectedGarmentsViewModel.class);

        donePlanningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (garmentsForOutfit.numGarments() < 2) {
                        Toast selectToast = Toast.makeText(getContext(), "Select at least 2 garments", Toast.LENGTH_SHORT);
                        selectToast.show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.navigation_review_outfit);
                    }
                }
        });
    }
}