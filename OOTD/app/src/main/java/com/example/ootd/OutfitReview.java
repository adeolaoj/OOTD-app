package com.example.ootd;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OutfitReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutfitReview extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button saveBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    public OutfitReview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OutfitReview.
     */
    // TODO: Rename and change types and number of parameters
    public static OutfitReview newInstance(String param1, String param2) {
        OutfitReview fragment = new OutfitReview();
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
        View view = inflater.inflate(R.layout.fragment_outfit_review, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.outfitReviewView);
        recyclerView.setPadding(0, 0, 0, 170);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        GarmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(GarmentViewModel.class);
        SelectedGarmentsViewModel garmentsForOutfit = new ViewModelProvider(requireActivity()).get(SelectedGarmentsViewModel.class);

        OutfitReviewAdapter adapter = new OutfitReviewAdapter(garmentsForOutfit, getContext());
        recyclerView.setAdapter(adapter);

        viewModel.getGarmentsData().observe(getViewLifecycleOwner(), garments ->{
            adapter.updateGarmentData(garmentsForOutfit);
        });

        saveBtn = view.findViewById(R.id.saveOutfitButton);
        saveBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.navigation_closet);
        });

        return view;
    }

    public static class OutfitReviewAdapter extends RecyclerView.Adapter<OutfitReviewAdapter.ViewHolder>{
        private SelectedGarmentsViewModel selectedGarments;
        private Context context;

        public OutfitReviewAdapter(SelectedGarmentsViewModel selectedGarments, Context context) {
            this.selectedGarments = selectedGarments;
            this.context = context;
        }

        public void updateGarmentData(SelectedGarmentsViewModel list) {
            this.selectedGarments = list;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            //private final TextView textView;
            private final ImageView imageView;
            //private final ChipGroup chipGroup;
            private final ImageButton favorite;
            private final CardView garmentCard;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                //chipGroup = view.findViewById(R.id.tagsList);
                imageView = view.findViewById(R.id.garmentImageView);
                favorite = view.findViewById(R.id.favorites);
                garmentCard = view.findViewById(R.id.garmentCard);
            }

//            public ChipGroup getChips() {
//                return chipGroup;
//            }

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
        public OutfitReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.garment_layout, viewGroup, false);

            return new OutfitReviewAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            Garment currGarment = selectedGarments.getGarment(position);

//            ChipGroup chipGroup = viewHolder.chipGroup;
//
//            chipGroup.removeAllViews();
//            for (String tag : currGarment.getGarmentTags()) {
//                Chip chip = new Chip(context);
//                chip.setText(tag);
//                chip.setCloseIconVisible(false);
//                chipGroup.addView(chip);
//            }

            ImageButton favoriteBtn = viewHolder.favorite;

            if (currGarment.isFavorite()) {
                favoriteBtn.setImageResource(R.drawable.favorites_filled);
            }

            // Get element from dataset at the corresponding positions and replace the
            // contents of the view with a picture of the garment
            //TODO: REPLACE WITH NON-PLACEHOLDER IMAGE
            viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return selectedGarments.numGarments();
        }

    }
}