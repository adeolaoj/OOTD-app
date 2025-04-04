package com.example.ootd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ootd.databinding.FragmentLoginBinding;
import com.example.ootd.databinding.FragmentNewUserBinding;


public class NewUserFragment extends Fragment {

    private Button signUpButton;
    private EditText email;
    private EditText password;
    private EditText name;

    private SharedPreferences myPrefs;
    private String savedEmail;
    private String savedPassword;
    private String savedName;
    private FragmentNewUserBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        signUpButton = binding.signUpButton;
        email = binding.signUpNewUserEmail;
        password = binding.signUpNewUserPassword;
        name = binding.signUpNewUserName;

        Context context = getActivity().getApplicationContext();
        myPrefs = context.getSharedPreferences("OOTD", Context.MODE_PRIVATE);

        // get saved info from prefs
        savedEmail = myPrefs.getString("loginEmail","");
        savedPassword = myPrefs.getString("loginPassword","");
        savedName = myPrefs.getString("signUpName","");

        // set info to text views
        email.setText(savedEmail);
        password.setText(savedPassword);
        name.setText(savedName);

        signUpButton.setOnClickListener(new View.OnClickListener() {
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