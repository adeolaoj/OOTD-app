package com.example.ootd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import static android.app.Activity.RESULT_OK;

import com.example.ootd.databinding.FragmentAddItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import android.net.Uri;

public class AddItemFragment extends Fragment {

    private FragmentAddItemBinding binding;

    ImageButton takePicture;
    ImageView imageView;
    Button saveButton;
    private static final int CAMERA_CODE = 100;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        takePicture = binding.imageButton;
        imageView = binding.imageDisplay;

        takePicture.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_CODE);
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            saveButton = binding.addItemSubmitButton;
            saveButton.setVisibility(View.VISIBLE);
            saveButton.setOnClickListener(v -> uploadImageToFirebaseStorage(photo));
        } else {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void uploadImageToFirebaseStorage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String path = "images/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(path);

        byte[] data = convertBitmapToByteArray(bitmap);

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getActivity(), "Upload successful!", Toast.LENGTH_SHORT).show();
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                if (uri != null) {
                    String downloadUrl =uri.toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("ImagePath", path);

                    GarmentListingFragment fragmentB = new GarmentListingFragment();
                    fragmentB.setArguments(bundle);

                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main); // Adjust the ID as per your layout
                    navController.navigate(R.id.action_addItemFragment_to_closetFragment, bundle);
                } else {
                    Log.d("Debug", "URI is null");
                }
            });
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
