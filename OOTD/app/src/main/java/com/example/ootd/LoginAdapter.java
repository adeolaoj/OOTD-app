package com.example.ootd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context ctx;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context ctx, int totalTabs) {
        super(fm);
        this.ctx = ctx;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new LoginFragment();
        } else {
            return new NewUserFragment();
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
