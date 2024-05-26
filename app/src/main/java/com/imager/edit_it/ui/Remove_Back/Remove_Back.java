package com.imager.edit_it.ui.Remove_Back;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imager.edit_it.databinding.RemoveBackBinding;
import com.imager.edit_it.ui.Converter.dialogwindow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Remove_Back extends Fragment {


    private static final int PICK_IMAGE = 1;
    private RemoveBackBinding binding;
    private ImageView imageViewBefore;
    private ImageView imageViewAfter;
    private Button buttonSave, btnchoosefile;
    private Uri selectedImageUri;
    private Bitmap resultBitmap;

    private TextView imagename;

    private String imageName, fileName_arancverj;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RemoveBackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageViewBefore = binding.imageViewBefore;
        imageViewAfter = binding.imageViewAfter;
        buttonSave = binding.buttonSave;
        btnchoosefile = binding.buttonOpenImage;

        btnchoosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToGallery();
            }
        });

        openGallery();

        return root;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewBefore.setImageBitmap(bitmap);
                removeBackground(selectedImageUri);

                String filePath = getRealPathFromURI(selectedImageUri);
                File file = new File(filePath);
                imageName = file.getName();
                fileName_arancverj = imageName.substring(0, imageName.lastIndexOf('.'));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeBackground(Uri imageUri) {
        String imagePath = getRealPathFromURI(imageUri);
        RemoveBgApi removeBgApi = new RemoveBgApi();
        removeBgApi.removeBackground(imagePath, new RemoveBgApi.RemoveBgCallback() {
            @Override
            public void onSuccess(byte[] image) {
                resultBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                getActivity().runOnUiThread(() -> imageViewAfter.setImageBitmap(resultBitmap));
            }

            @Override
            public void onFailure(String errorMessage) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to remove background: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    private void saveImageToGallery() {
        if (resultBitmap != null) {
            FileOutputStream fos = null;
            try {
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/ConverterImage";
                File file = new File(imagesDir);
                if (!file.exists()) {
                    file.mkdirs();
                }

                // Use UUID to generate unique filename
                String fileName = "IMG_" + fileName_arancverj + "_" + UUID.randomUUID().toString() + ".png";
                File image = new File(imagesDir, fileName);
                fos = new FileOutputStream(image);
                resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    uploadAndSaveImage(resultBitmap);
                }
                fos.flush();
                addImageToGallery(image.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(getContext(), "No image to save", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAndSaveImage(Bitmap bitmap) {
        DatabaseReference firebaseDatabase;
        FirebaseDatabase Database = FirebaseDatabase.getInstance("https://edit-it-v2-default-rtdb.europe-west1.firebasedatabase.app/");
        firebaseDatabase = Database.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && bitmap != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            String imagenamefirebase = fileName_arancverj;
            String imageId = UUID.randomUUID().toString();
            StorageReference imagesRef = storageRef.child("images/" + user.getUid() + "/" + imageId + "|" + imagenamefirebase);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageData = baos.toByteArray();

            UploadTask uploadTask = imagesRef.putBytes(imageData);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        DatabaseReference userRef = firebaseDatabase.child("users").child(user.getUid()).child("profileImageUrls");
                        String imageKey = userRef.push().getKey();
                        userRef.child(imageKey).setValue(imageUrl)
                                .addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Image URL saved to database.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to save image URL to database.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to retrieve download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to upload image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "User or image is null.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addImageToGallery(String filePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
