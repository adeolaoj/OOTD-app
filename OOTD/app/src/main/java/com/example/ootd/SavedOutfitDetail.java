package com.example.ootd;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedOutfitDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedOutfitDetail extends Fragment {

    private RecyclerView recyclerView;
    private List<Garment> outfitGarment;
    private String outfitName;
    private Context context;


    public SavedOutfitDetail() {
        // Required empty public constructor
    }

    public static SavedOutfitDetail newInstance(ArrayList<Garment> garments, String name) {
        SavedOutfitDetail fragment = new SavedOutfitDetail();
        Bundle args = new Bundle();
        args.putSerializable("garments", garments);
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            outfitGarment = (ArrayList<Garment>) getArguments().getSerializable("garments");
            outfitName = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_outfit_detail, container, false);
        recyclerView = view.findViewById(R.id.savedOutfitRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new SavedOutfitDetailAdapter(outfitGarment, getContext()));
        return view;
    }

    public static class SavedOutfitDetailAdapter extends RecyclerView.Adapter<SavedOutfitDetailAdapter.ViewHolder> {
        private List<Garment> garments;
        private Context context;

        public SavedOutfitDetailAdapter(List<Garment> garments, Context context) {
            this.garments = garments;
            this.context = context;
        }

        @NonNull
        @Override
        public SavedOutfitDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.garment_layout, parent, false);
            return new SavedOutfitDetailAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Garment garment = garments.get(position);

            StorageReference sref = FirebaseStorage.getInstance().getReference();
            sref.child(garment.getImagePath()).getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(context).load(uri).into(holder.imageView);
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Could not load image.",
                        Toast.LENGTH_SHORT).show();
                holder.imageView.setImageResource(R.drawable.garment_picture_default);
            });

            holder.favoriteButton.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return garments.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageButton favoriteButton;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.garmentImageView);
                favoriteButton = itemView.findViewById(R.id.favorites);
            }
        }
    }
}