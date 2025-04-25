package com.example.ootd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ootd.databinding.FragmentLoginBinding;
import com.example.ootd.databinding.FragmentNewUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;


public class NewUserFragment extends Fragment {

    private Button signUpButton;
    private String email;
    private String password;
    private String confirmation;
    private String name;
    private FragmentNewUserBinding binding;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        signUpButton = binding.signUpButton;

        Context context = getActivity().getApplicationContext();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.signUpNewUserName.getText().toString();
                email = binding.signUpNewUserEmail.getText().toString();
                password = binding.signUpNewUserPassword.getText().toString();
                confirmation = binding.signUpNewUserConfirmPassword.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Missing Name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Missing Email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Missing Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (confirmation.isEmpty()) {
                    Toast.makeText(getActivity(), "Missing Password Confirmation",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (confirmation.equals(password)) {
                    createAccount(name, email,password);
                } else {
                    Toast.makeText(getActivity(), "Passwords Non-Matching",
                            Toast.LENGTH_SHORT).show();
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

    private void createAccount(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignUp", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference user_ref = database.getReference("users");
                            DatabaseReference uid_ref = user_ref.child(user.getUid());
                            uid_ref.setValue(name);

                            DatabaseReference dbref = database.getReference("data");
                            DatabaseReference name_ref = dbref.child(name);
                            Map<String, Object> info = new HashMap<>();
                            info.put("uid", user.getUid());
                            info.put("email", email);
                            info.put("garments",null);

                            name_ref.setValue(info);

                            Bundle bdl = new Bundle();
                            bdl.putString("Username",name);

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtras(bdl);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            if (!task.isSuccessful()) {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getActivity(), "Email already in use. Please log in instead.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Sign up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }
}