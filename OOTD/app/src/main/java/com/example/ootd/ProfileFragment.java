package com.example.ootd;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ootd.databinding.FragmentProfileBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentProfileBinding binding;
    private Button logOutButton;
    private Button stinkerButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // log out button logic
        logOutButton = binding.logOutButton;
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        stinkerButton = binding.stinkerButton;
        stinkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilter();
            }
        });

        return root;
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