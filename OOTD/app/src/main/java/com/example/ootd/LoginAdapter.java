package com.example.ootd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/*
assistance from youtube video: https://www.youtube.com/watch?app=desktop&v=ayKMfVt2Sg4
 */

public class LoginAdapter extends FragmentStateAdapter {
    private Context ctx;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Lifecycle lifecycle, int totalTabs) {
        super(fm, lifecycle);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LoginFragment();
        } else {
            return new NewUserFragment();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
