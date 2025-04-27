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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ootd.databinding.FragmentClosetLandingItemListingBinding;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClosetLanding_ItemListing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClosetLanding_ItemListing extends Fragment {

    public static final int MENU_ITEM_EDIT = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;
    Context cntx;
    private MainActivity myact;


    private List<Garment> garmentList;

    private FragmentClosetLandingItemListingBinding binding;
    private RecyclerView recyclerView;
    private GarmentAdapter adapter;

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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        cntx = getContext();

        // Add menu items
        menu.add(0, MENU_ITEM_EDIT, 0, R.string.edit_listing);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.delete_listing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentClosetLandingItemListingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //recyclerView = view.findViewById(R.id.garmentRecyclerView);
        recyclerView = root.findViewById(R.id.garmentRecyclerView);
        recyclerView.setPadding(0, 0, 0, 160);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        GarmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(GarmentViewModel.class);

        //GarmentAdapter adapter = new GarmentAdapter(new ArrayList<>(), getContext());
        adapter = new GarmentAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        // set up the data, get the data from the ViewModel
        viewModel.getGarmentsData().observe(getViewLifecycleOwner(), garments ->{
            adapter.updateGarmentData(garments);
        });

        recyclerView.setOnCreateContextMenuListener(this);

        ImageButton filterButton = binding.filterButtonItems;
        filterButton.setOnClickListener(v -> {
            openFilter();
        });

        // Inflate the layout for this fragment
        //return view;
        return root;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (!isVisible()) {
            return false;
        }

        super.onContextItemSelected(item);

        int positionCurr = adapter.getCurrPosition();

        if (positionCurr < 0 || positionCurr >= adapter.getItemCount()) {
            Toast.makeText(requireContext(), "Error: No garment selected.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (item.getItemId()) {
            case MENU_ITEM_EDIT: {
                int currPosition = adapter.getCurrPosition();

                Garment curr = adapter.getGarmentAt(currPosition);

                Bundle bundle = new Bundle();
                bundle.putString("key", curr.getKey());
                bundle.putString("ImagePath",curr.getImagePath());
                Log.d("ImagePath", "Image Path before passing to bundle: " + curr.getImagePath());
                bundle.putString("Category", curr.getCategory());
                bundle.putString("Subcategory", curr.getSubcategory());
                ArrayList<String> colorTagsBruh = (ArrayList<String>) curr.getColorTags();

                bundle.putStringArrayList("ColorTags", colorTagsBruh);

                Navigation.findNavController(this.getView()).navigate(R.id.navigation_garment_listing, bundle);
                return false;
            }
            case MENU_ITEM_DELETE: {
                int position = adapter.getCurrPosition();
                Garment toDelete = adapter.getGarmentAt(position);

                if (toDelete.getKey() == null) {
                    Toast.makeText(requireContext(), "Error: Garment key not found",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

                userRef.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String username = snapshot.getValue(String.class);

                        if (username != null) {
                            DatabaseReference garmentRef = FirebaseDatabase.getInstance()
                                    .getReference("data")
                                    .child(username)
                                    .child("garments")
                                    .child(toDelete.getKey());

                            garmentRef.removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(requireContext(), "Garment deleted", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(requireContext(), "Delete unsuccessful", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(requireContext(), "Username not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error retrieving user", Toast.LENGTH_SHORT).show();
                });

                return true;
            }
        }
        return false;
    }

    public class GarmentAdapter extends RecyclerView.Adapter<GarmentAdapter.ViewHolder> {
        private List<Garment> garmentList;
        private Context context;

        private int currPosition = -1;

        public GarmentAdapter(List<Garment> dataSet, Context context) {
            this.garmentList = dataSet;
            this.context = context;
        }

        public Garment getGarmentAt(int position) {
            return garmentList.get(position);
        }

        public void updateGarmentData(List<Garment> newData) {
            this.garmentList = newData;
            notifyDataSetChanged();  // Notifying the adapter to re-render the data
        }

        public int getCurrPosition() {
            return currPosition;
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
            Log.e("StorageDebug", "Trying to load imagePath: " + imagePath);

            if (imagePath != null && !imagePath.isEmpty()) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child(imagePath);

                imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            Glide.with(viewHolder.imageView.getContext())
                                    .load(uri)
                                    .placeholder(R.drawable.garment_picture_default) // <-- sets default while loading
                                    .error(R.drawable.garment_picture_default)       // <-- fallback if error
                                    .into(viewHolder.getImageView());
                        })
                        .addOnFailureListener(e -> {
                            viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);
                        });
            } else {
                viewHolder.getImageView().setImageResource(R.drawable.garment_picture_default);
            }


            if (imagePath != null && !imagePath.isEmpty()) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child(imagePath);

                imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            Glide.with(viewHolder.imageView.getContext())
                                    .load(uri)
                                    .placeholder(R.drawable.garment_picture_default)
                                    .error(R.drawable.garment_picture_default)
                                    .into(viewHolder.getImageView());
                        })
                        .addOnFailureListener(e -> {
                            Log.e("StorageDebug", "Failed to load imagePath: " + imagePath, e);
                        });
            }


            if (garment.getGarmentTags() != null) {
                for (String tag : garment.getGarmentTags()) {
                    Chip chip = new Chip(context);
                    chip.setText(tag);
                    chip.setCloseIconVisible(false);
                    chipGroup.addView(chip);
                }
            }

            if (garment.getColorTags() != null) {
                for (String color: garment.getColorTags()) {
                    Chip chip = new Chip(context);
                    chip.setText(color);
                    chip.setCloseIconVisible(false);
                    chipGroup.addView(chip);
                }
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userRef.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String username = snapshot.getValue(String.class);
                    if (username != null) {

                        if (garment.getKey() == null) {
                            Log.e("FavoriteError", "Garment key is null for garment: " + garment.getImagePath());
                            viewHolder.favorite.setImageResource(R.drawable.favorites_unfilled);
                            return;
                        }

                        DatabaseReference garmentRef = FirebaseDatabase.getInstance()
                                .getReference("data")
                                .child(username)
                                .child("garments")
                                .child(garment.getKey())
                                .child("favorites");

                        garmentRef.get().addOnSuccessListener(favSnapshot -> {
                            Boolean isFavorite = favSnapshot.getValue(Boolean.class);
                            if (isFavorite != null && isFavorite) {
                                viewHolder.favorite.setImageResource(R.drawable.favorites_filled);
                            } else {
                                viewHolder.favorite.setImageResource(R.drawable.favorites_unfilled);
                            }
                        }).addOnFailureListener(e -> {
                            viewHolder.favorite.setImageResource(R.drawable.favorites_unfilled);
                        });
                    }
                }
            });


            ImageButton favoriteBtn = viewHolder.favorite;
            favoriteBtn.setOnClickListener(v -> {
                FirebaseAuth auth2 = FirebaseAuth.getInstance();
                String uid2 = auth2.getUid();

                DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference("users").child(uid2);

                userRef2.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String username2 = snapshot.getValue(String.class);

                        if (username2 != null) {
                            DatabaseReference garmentRef2 = FirebaseDatabase.getInstance()
                                    .getReference("data")
                                    .child(username2)
                                    .child("garments")
                                    .child(garment.getKey())
                                    .child("favorites");

                            garmentRef2.get().addOnSuccessListener(favSnapshot -> {
                                Boolean isFavorite = favSnapshot.getValue(Boolean.class);
                                if (isFavorite != null && isFavorite) {
                                    garmentRef2.setValue(false);
                                    favoriteBtn.setImageResource(R.drawable.favorites_unfilled);
                                } else {
                                    garmentRef2.setValue(true);
                                    favoriteBtn.setImageResource(R.drawable.favorites_filled);
                                }
                            });
                        }
                    }
                });
            });

            viewHolder.itemView.setOnLongClickListener(v -> {
                currPosition = viewHolder.getAdapterPosition();
                v.showContextMenu();
                return true;
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

    private void openFilter() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());

        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                R.layout.filter_bottom_sheet_garments,
                getActivity().findViewById(android.R.id.content),
                false
        );
        bottomSheetDialog.setContentView(bottomSheetView);

        // visibility for subcategories so the sort and filter screen isn't extremely long
        ChipGroup categoryChipGroup = bottomSheetView.findViewById(R.id.categoryChipGroup);
        TextView subcategoryText = bottomSheetView.findViewById(R.id.SortFilterSubcategory);
        ChipGroup colorChipGroup = bottomSheetView.findViewById(R.id.colorChipGroup);
        CheckBox favorites = bottomSheetView.findViewById(R.id.checkboxFavorites);

        // tops
        Chip topsChip = bottomSheetView.findViewById(R.id.topsChip);
        ChipGroup topSubcategoryChipGroup = bottomSheetView.findViewById(R.id.topSubcategoryChipGroup);
        // bottoms
        Chip bottomsChip = bottomSheetView.findViewById(R.id.bottomsChip);
        ChipGroup bottomsSubcategoryChipGroup = bottomSheetView.findViewById(R.id.bottomsSubcategoryChipGroup);
        // shoes
        Chip shoesChip = bottomSheetView.findViewById(R.id.shoesChip);
        ChipGroup shoesSubcategoryChipGroup = bottomSheetView.findViewById(R.id.shoesSubcategoryChipGroup);
        // outerwear
        Chip outerwearChip = bottomSheetView.findViewById(R.id.outerwearChip);
        ChipGroup outerwearSubcategoryChipGroup = bottomSheetView.findViewById(R.id.outerwearSubcategoryChipGroup);
        // dresses
        Chip dressesChip = bottomSheetView.findViewById(R.id.dressesChip);
        ChipGroup dressesSubcategoryChipGroup = bottomSheetView.findViewById(R.id.dressesSubcategoryChipGroup);
        // swim
        Chip swimChip = bottomSheetView.findViewById(R.id.swimChip);
        ChipGroup swimSubcategoryChipGroup = bottomSheetView.findViewById(R.id.swimSubcategoryChipGroup);
        // accessories
        Chip accessoriesChip = bottomSheetView.findViewById(R.id.accessoriesChip);
        ChipGroup accessoriesSubcategoryChipGroup = bottomSheetView.findViewById(R.id.accessoriesSubcategoryChipGroup);
        // jewelry
        Chip jewelryChip = bottomSheetView.findViewById(R.id.jewelryChip);
        ChipGroup jewelrySubcategoryChipGroup = bottomSheetView.findViewById(R.id.jewelrySubcategoryChipGroup);
        // bags
        Chip bagChip = bottomSheetView.findViewById(R.id.bagsChip);
        ChipGroup bagSubcategoryChipGroup = bottomSheetView.findViewById(R.id.bagsSubcategoryChipGroup);
        // headwear
        Chip headwearChip = bottomSheetView.findViewById(R.id.headwearChip);
        ChipGroup headwearSubcategoryChipGroup = bottomSheetView.findViewById(R.id.hatsSubcategoryChipGroup);

        // change visibility
        subcategoryText.setVisibility(View.GONE);

        categoryChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (topsChip.isChecked()) {
                    topSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    topSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (bottomsChip.isChecked()) {
                    bottomsSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    bottomsSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (shoesChip.isChecked()) {
                    shoesSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    shoesSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (outerwearChip.isChecked()) {
                    outerwearSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    outerwearSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (dressesChip.isChecked()) {
                    dressesSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    dressesSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (swimChip.isChecked()) {
                    swimSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    swimSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (accessoriesChip.isChecked()) {
                    accessoriesSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    accessoriesSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (jewelryChip.isChecked()) {
                    jewelrySubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    jewelrySubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (bagChip.isChecked()) {
                    bagSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    bagSubcategoryChipGroup.setVisibility(View.GONE);
                }
                if (headwearChip.isChecked()) {
                    headwearSubcategoryChipGroup.setVisibility(View.VISIBLE);
                    subcategoryText.setVisibility(View.VISIBLE);
                } else {
                    headwearSubcategoryChipGroup.setVisibility(View.GONE);
                }
            }
        });

        // make the stuff behind the filter window darker
        if (bottomSheetDialog.getWindow() != null) {
            bottomSheetDialog.getWindow().setDimAmount(0.7f);
        }

        // when press clear filters
        bottomSheetView.findViewById(R.id.clearFilterButton).setOnClickListener(v -> {
            categoryChipGroup.clearCheck();
            colorChipGroup.clearCheck();
            favorites.setChecked(false);
        });

        // when press apply filters
        bottomSheetView.findViewById(R.id.applyFilterButton).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }


}