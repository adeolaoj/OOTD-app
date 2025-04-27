package com.example.ootd;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClosetLanding_OutfitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClosetLanding_OutfitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private RecyclerView recyclerView;
    private GarmentViewModel viewModel;

    public ClosetLanding_OutfitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClosetLanding_OutfitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClosetLanding_OutfitFragment newInstance(String param1, String param2) {
        ClosetLanding_OutfitFragment fragment = new ClosetLanding_OutfitFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GarmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_closet_landing__outfit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.outfitsRecyclerView);
        recyclerView.setPadding(0, 0, 0,160);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        outfitAdapter adapter = new outfitAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getSavedOutfits().observe(getViewLifecycleOwner(), outfits -> {
            adapter.updateOutfitData(outfits);
        });

    }

    public static class outfitAdapter extends RecyclerView.Adapter<outfitAdapter.ViewHolder> {
        private List<Outfit> outfits = new ArrayList<>();
        private Context context;
        private OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public void updateOutfitData(List<Outfit> outfits) {
            this.outfits = outfits;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_outfit,
                    parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Outfit outfit = outfits.get(position);
            List<Garment> garments = outfit.getOutfitGarments();

            holder.outfitNameTextView.setText(outfit.getOutfitName());

            GridLayout mainGrid = holder.itemView.findViewById(R.id.mainGrid);
            LinearLayout bottomRow = holder.itemView.findViewById(R.id.extraRow);

            mainGrid.removeAllViews();
            bottomRow.removeAllViews();

            int padding = 4;
            int index = 0;

            for (Garment garment:garments) {
                SquareImageView imageView = new SquareImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child(garment.getImagePath()).getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.garment_picture_default)
                            .into(imageView);
                }).addOnFailureListener(e -> {
                    Log.e("ImageLoadError", "Could not load image: " + e.getMessage());
                    imageView.setImageResource(R.drawable.garment_picture_default);
                });


                if (index < 4) {
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.rowSpec = GridLayout.spec(index / 2, 1f);
                    params.columnSpec = GridLayout.spec(index % 2, 1f);
                    params.width = 0;
                    params.height = 0;
                    params.setMargins(4, 4, 4, 4);
                    imageView.setLayoutParams(params);
                    mainGrid.addView(imageView);
                } else {
                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    int screenWidth = metrics.widthPixels;

                    int cardPadding = 200;
                    int maxItems = garments.size() - 4;

                    int availableWidth = screenWidth - cardPadding;
                    int bottomSize = availableWidth / Math.min(maxItems, 5);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bottomSize, bottomSize);
                    params.setMargins(padding, 0, padding, 0);
                    bottomRow.addView(imageView, params);
                }


                index++;
            }
        }

        @Override
        public int getItemCount() {
            return outfits.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            GridLayout mainGrid;
            LinearLayout bottomRow;
            private TextView outfitNameTextView;


            ViewHolder(View itemView) {
                super(itemView);
                mainGrid = itemView.findViewById(R.id.mainGrid);
                bottomRow = itemView.findViewById(R.id.extraRow);
                outfitNameTextView = itemView.findViewById(R.id.outfitNameTextView);
            }

        }
    }

    public static class SquareImageView extends AppCompatImageView {
        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}