package com.example.ootd;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_user);


        /*
        TabLayout loginTabLayout = findViewById(R.id.loginTabs);
        ViewPager2 viewPager = findViewById(R.id.loginViewPager);

        new TabLayoutMediator(loginTabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Login");
            } else {
                tab.setText("New User");
            }
        }).attach();

         */
    }
}