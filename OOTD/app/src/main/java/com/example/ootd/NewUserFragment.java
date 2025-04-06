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
import com.google.firebase.auth.FirebaseUser;


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
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

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
                String email = binding.signUpNewUserEmail.getText().toString();
                String password = binding.signUpNewUserPassword.getText().toString();
                String password_confirm = binding.signUpNewUserConfirmPassword.getText().toString();

                if (password_confirm.equals(password)) {
                    createAccount(email,password);
                }

                Toast.makeText(getActivity(), "Passwords Mismatching",
                        Toast.LENGTH_SHORT).show();

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignUp", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}