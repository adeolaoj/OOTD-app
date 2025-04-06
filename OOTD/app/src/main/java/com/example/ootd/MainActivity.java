package com.example.ootd;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.ootd.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_closet, R.id.navigation_outfit_log, R.id.navigation_add_plan, R.id.navigation_generate, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        /*
        Context context = getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences("LIBRARY", MODE_PRIVATE);
        String name = myPrefs.getString("loginName", "Owner");


         */
        EdgeToEdge.enable(this);

        // setting up a list of garments (temporary data):
        List<Garment> garments = new ArrayList<>();
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Casual",
                "Winter", "Long-Sleeves"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Formal",
                "Summer", "Short-Sleeves"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Semi-Formal",
                "Fall", "Sleeveless"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Basic",
                "Winter", "Long-Sleeves"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Boxy",
                "Summer", "Short-Sleeves"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Baggy",
                "Fall", "Sleeveless"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Business",
                "Winter", "Long-Sleeves"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Pajamas",
                "Summer", "Short-Sleeves"))));
        garments.add(new Garment(R.drawable.garment_picture_default, new ArrayList<String>(List.of("Colorful",
                "Fall", "Sleeveless", "Supercute", "amazing"))));

        garments.get(2).setFavorites();

        // Initializing the data by referencing the GarmentViewModel:
        GarmentViewModel viewModel = new ViewModelProvider(this).get(GarmentViewModel.class);
        viewModel.setGarmentsData(garments); // TODO: replace garments with actual database stuff later

    }

    // implement the back button for fragments
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return (navController.navigateUp() || super.onSupportNavigateUp());
    }
}