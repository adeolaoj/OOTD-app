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

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment {

    private Button loginButton;
    private EditText email;
    private EditText password;

    private SharedPreferences myPrefs;
    private String savedEmail;
    private String savedPassword;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // authentication for sprint 2
        //mAuth = FirebaseAuth.getInstance();

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

                //loginUser(email, password);
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

    // fix authentication for sprint 2
    /*
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Auth", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Update UI or proceed to next activity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Auth", "signInWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Invalid Sign In", Toast.LENGTH_SHORT).show();
                    }
                });
    }
     */


}