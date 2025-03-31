package com.example.ootd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ootd.databinding.FragmentClosetBinding;

public class ClosetFragment extends Fragment {

    private FragmentClosetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

          // COMMENTED OUT TO SIMPLIFY
 //       ChatViewModel notificationsViewModel =
 //               new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // ADDED THIS LINE TO AVOID USING THE ChatViewModel class
        binding.textChat.setText("This is the chat tab.");

          // COMMENTED OUT TO SIMPLIFY
 //       final TextView textView = binding.textNotifications;
 //       notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}