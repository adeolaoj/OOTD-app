package com.example.ootd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ootd.databinding.FragmentLoginBinding;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment {

    private Button loginButton;
    private EditText name;
    private EditText password;

    private SharedPreferences myPrefs;
    private String savedEmail;
    private String savedPassword;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginButton = binding.loginButton;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.loginUserName.getText().toString();
                Log.d("Auth", "Name: " + name);
                String password = binding.loginUserPassword.getText().toString();
                Log.d("Auth", "Password: " + password);

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter username", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    lookup(name, password);
                }

            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void lookup(String username, String password) {
        DatabaseReference dbref = FirebaseDatabase.getInstance()
                .getReference("data")
                .child(username);

        Log.d("Auth", "dbref: " + dbref.getKey());


        dbref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String email = task.getResult().child("email").getValue(String.class);
                Log.d("Auth", "Email: " + email);
                Log.d("Auth", "Username found!");
                loginUser(username,email,password);
            } else {
                Toast.makeText(getActivity(), "Username not found", Toast.LENGTH_SHORT).show();
                Log.d("Auth", "Username not found!");
            }
        });
    }

    public void loginUser(String name, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        Log.d("Auth", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        Bundle bdl = new Bundle();
                        bdl.putString("Username", name);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtras(bdl);

                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Log.w("Auth", "signInWithEmail:failure", task.getException());

                        String errorMessage = "Invalid Sign In"; 

                        Exception exception = task.getException();
                        if (exception != null) {
                            String error = exception.getMessage();
                            if (error != null) {
                                if (error.contains("password is invalid") || error.contains("INVALID_PASSWORD")) {
                                    errorMessage = "Incorrect password";
                                } else if (error.contains("no user record") || error.contains("EMAIL_NOT_FOUND")) {
                                    errorMessage = "Username not found";
                                }
                            }
                        }

                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }



}