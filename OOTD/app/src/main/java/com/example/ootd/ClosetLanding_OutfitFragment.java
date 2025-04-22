package com.example.ootd;

import android.os.Bundle;
import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClosetLanding_OutfitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClosetLanding_OutfitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            if (!outfits.isEmpty()) {
               // holder.imageView.setImageURI(outfit.get(0).getImageAddress());
            }
            List<Garment> garments = outfit.getOutfitGarments();
            GridLayout imageGrid = holder.imageGrid;
            imageGrid.removeAllViews();

            for (Garment garment:garments) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.garment_picture_default);
                imageGrid.addView(imageView);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 200;
                params.height = 200;
                params.setMargins(5, 5, 5, 5);
                imageView.setLayoutParams(params);
            }
        }

        @Override
        public int getItemCount() {
            return outfits.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            GridLayout imageGrid;

            ViewHolder(View itemView) {
                super(itemView);
                imageGrid = itemView.findViewById(R.id.garmentGrid);
            }

        }
    }
}