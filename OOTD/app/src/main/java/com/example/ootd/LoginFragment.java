package com.example.ootd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ootd.databinding.FragmentLoginBinding;
import com.google.android.material.tabs.TabLayout;

public class LoginFragment extends Fragment {

    private Button loginButton;
    private EditText email;
    private EditText password;

    private SharedPreferences myPrefs;
    private String savedEmail;
    private String savedPassword;
    private FragmentLoginBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View root = inflater.inflate(R.layout.fragment_new_user, container, false);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginButton = binding.loginButton;
        email = binding.loginUserEmail;
        password = binding.loginUserPassword;

        Context context = getActivity().getApplicationContext();
        myPrefs = context.getSharedPreferences("OOTD", Context.MODE_PRIVATE);

        // get saved info from prefs
        savedEmail = myPrefs.getString("loginEmail","");
        savedPassword = myPrefs.getString("loginPassword","");

        // set info to text views
        email.setText(savedEmail);
        password.setText(savedPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.putString("loginEmail", String.valueOf(email.getText()));
                peditor.putString("loginPassword", String.valueOf(password.getText()));
                peditor.apply();

                // login
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}