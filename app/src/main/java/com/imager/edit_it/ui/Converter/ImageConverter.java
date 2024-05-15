package com.imager.edit_it.ui.Converter;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imager.edit_it.R;
import com.imager.edit_it.databinding.ImageformatBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.imager.edit_it.databinding.SignInBinding;
import com.imager.edit_it.ui.Login_reg.Cloud_Sing_in;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class ImageConverter extends Fragment {


    private static final int REQUEST_PERMISSION_CODE = 123;

    boolean true_or_false = false, save_cloud = false;

    private ImageformatBinding binding;
    private ImageView imageView;
    private static final int PICK_IMAGE = 1;
    SeekBar seekBar, seekBar1;
    int tapancik = 255;

    int vorak = 100;
    LinearLayout seekBar_converter;
    String converter_iamge_1;
    Bitmap.CompressFormat converter_iamge_2;
    Uri selectedImageUri;
    LinearLayout layaut_converter_before_after;
    TextView converter_before;

    TextView converter_after;


    TextInputLayout textInputLayout;

    Button colorpicker, openGalleryButton, convertAndSaveGallery;

    RadioButton radioButton;

    RadioGroup converter_radio;

    Switch on_off_cuectal;


    public String login, password;




    private FirebaseAuth mAuth;

    private DatabaseReference firebaseDatabase;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ImageformatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        seekBar = root.findViewById(R.id.vorak_coverter);
        seekBar1 = root.findViewById(R.id.transparent_coverter);

        seekBar1.setMax(255);
        seekBar.setProgress(100);
        seekBar1.setProgress(255);



        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {

                    tapancik = seekBar1.getProgress();
                    tesnel_image();
                }


        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vorak = seekBar.getProgress();
                tesnel_image();


            }
        });

        on_off_cuectal = root.findViewById(R.id.switch1);

        textInputLayout = root.findViewById(R.id.father_file_name_refractor);

        converter_after = root.findViewById(R.id.converter_after);
        converter_before = root.findViewById(R.id.converter_before);
        layaut_converter_before_after = root.findViewById(R.id.layaut_before_after_converter);
        seekBar_converter = root.findViewById(R.id.seekbar_converter);

        converter_radio = root.findViewById(R.id.converter_radio);

        RadioButton pngRadioButton = root.findViewById(R.id.png_converter);
        RadioButton jpgRadioButton = root.findViewById(R.id.jpg_converter);
        RadioButton jpegRadioButton = root.findViewById(R.id.jpeg_converter);
        RadioButton webpRadioButton = root.findViewById(R.id.webp_converter);
        RadioButton bmpRadioButton = root.findViewById(R.id.bmp_converter);

        pngRadioButton.setOnClickListener(this::onRadioButtonClicked);
        jpgRadioButton.setOnClickListener(this::onRadioButtonClicked);
        jpegRadioButton.setOnClickListener(this::onRadioButtonClicked);
        webpRadioButton.setOnClickListener(this::onRadioButtonClicked);
        bmpRadioButton.setOnClickListener(this::onRadioButtonClicked);
        imageView = root.findViewById(R.id.imageView);
        openGalleryButton = root.findViewById(R.id.openGalleryButton);
        convertAndSaveGallery = root.findViewById(R.id.button_choose_verj);
        colorpicker = root.findViewById(R.id.colorpicker);

        on_off_cuectal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(on_off_cuectal.isChecked() == true){
                    tesnel_image();
                }

            }
        });


        convertAndSaveGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery();
            }
        });
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

        });
        colorpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://edit-it-v2-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        checkSavedUser();
        return root;
    }

    public void checkSavedUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        login = preferences.getString("email", null);
        password = preferences.getString("password", null);
        if (login != null && password != null) {

            signInWithEmailAndPassword(login, password);

        }
    }


    public void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    save_cloud = true;
                }
            } else {
                Toast.makeText(getActivity(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            converter_radio.clearCheck();
            true_or_false = false;
            selectedImageUri = data.getData();
            File file = new File(selectedImageUri.getPath());
            converter_before.setText(String.valueOf(file.length()));

            seekBar.setProgress(100);
            seekBar1.setProgress(255);
            tapancik = 255;
            vorak = 100;
            imageView.setImageURI(data.getData());


            tesnel_image();

            String filePath = null;

            if (selectedImageUri != null && "content".equals(selectedImageUri.getScheme())) {
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    filePath = cursor.getString(idx);
                    cursor.close();
                }
            } else {
                filePath = selectedImageUri.getPath();
            }

            File file1 = new File(filePath);
            double glav_file_before = (double) file1.length() / (1024 * 1024);
            String glav_before_string = String.format("%.2f", glav_file_before);
            converter_before.setText("Before:" + glav_before_string + "MB");
            converter_after.setText("After:" + glav_before_string + "MB");

            String porcnakan123 = file1.getName();
            String fileName_arancverj = porcnakan123.substring(0, porcnakan123.lastIndexOf('.'));
            textInputLayout.getEditText().setText(fileName_arancverj);




        }
    }

    public void onRadioButtonClicked(View view) {
        radioButton = (RadioButton) view;
        boolean checked = radioButton.isChecked();
        String text = radioButton.getText().toString();
        switch (text) {
            case "png":
                if (checked) {
                    layaut_converter_before_after.setVisibility(View.VISIBLE);
                    seekBar_converter.setVisibility(View.GONE);
                    colorpicker.setVisibility(View.VISIBLE);
                    converter_iamge_1 = ".png";
                    converter_iamge_2 = Bitmap.CompressFormat.PNG;
                    tesnel_image();
                    break;
                }
            case "jpg":
                if (checked) {
                    layaut_converter_before_after.setVisibility(View.VISIBLE);
                    seekBar_converter.setVisibility(View.VISIBLE);
                    colorpicker.setVisibility(View.GONE);
                    converter_iamge_1 = ".jpg";
                    converter_iamge_2 = Bitmap.CompressFormat.JPEG;
                    tesnel_image();
                    break;
                }


            case "jpeg":
                if (checked) {
                    layaut_converter_before_after.setVisibility(View.VISIBLE);
                    seekBar_converter.setVisibility(View.VISIBLE);
                    colorpicker.setVisibility(View.GONE);
                    converter_iamge_1 = ".jpeg";
                    converter_iamge_2 = Bitmap.CompressFormat.JPEG;
                    tesnel_image();
                    break;
                }

            case "webp":
                if (checked) {
                    layaut_converter_before_after.setVisibility(View.VISIBLE);
                    seekBar_converter.setVisibility(View.VISIBLE);
                    colorpicker.setVisibility(View.VISIBLE);
                    converter_iamge_1 = ".webp";
                    converter_iamge_2 = Bitmap.CompressFormat.WEBP;
                    tesnel_image();
                    break;

                }
            case "bmp":
                if (checked) {
                    layaut_converter_before_after.setVisibility(View.VISIBLE);
                    seekBar_converter.setVisibility(View.GONE);
                    colorpicker.setVisibility(View.VISIBLE);
                    converter_iamge_1 = ".bmp";
                    converter_iamge_2 = Bitmap.CompressFormat.PNG;
                    tesnel_image();
                    break;

                }
        }
    }

    public int color1;

    private void showColorPickerDialog() {
        Colorpickerwindow dialog = new Colorpickerwindow();
        dialog.show(getParentFragmentManager(), "CustomColorPickerDialogFragment");

        dialog.setOnColorPickedListener(new Colorpickerwindow.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color) {
                color1 = color;
                tesnel_image();
                true_or_false = true;


            }
        });

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performSaveToGallery();
            } else {
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void anjatel(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            child.setClickable(false);
            child.setFocusable(false);
            child.setEnabled(false);
        }
    }

    private void miacnel(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            child.setClickable(true);
            child.setFocusable(true);
            child.setEnabled(true);
        }
    }

    private void tesnel_image() {
        if (selectedImageUri != null && converter_iamge_2 != null && on_off_cuectal.isChecked() == true) {
            seekBar.setEnabled(false);
            seekBar1.setEnabled(false);
            convertAndSaveGallery.setEnabled(false);
            colorpicker.setEnabled(false);
            openGalleryButton.setEnabled(false);
            anjatel(converter_radio);
            new Thread(() -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    if (true_or_false == true) {
                        bitmap = tree_changealpha_to_color(bitmap, color1);
                    }

                    bitmap.compress(converter_iamge_2, vorak, byteArrayOutputStream);

                    byte[] byteArray = byteArrayOutputStream.toByteArray();


                    requireActivity().runOnUiThread(() -> {
                        try {
                            seekBar.setEnabled(true);
                            seekBar1.setEnabled(true);
                            convertAndSaveGallery.setEnabled(true);
                            colorpicker.setEnabled(true);
                            openGalleryButton.setEnabled(true);
                            miacnel(converter_radio);
                            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            true_or_false = true;
                            imageView.setImageBitmap(decodeBitmap);
                            imageView.setAlpha(tapancik);
                            int glavBytes = byteArray.length;
                            double glavFileAfter = glavBytes / (1024.0 * 1024.0);
                            String glavAfterString = String.format("%.2f", glavFileAfter);
                            converter_after.setText("After:" + glavAfterString + "MB");

                           
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }


    private void performSaveToGallery() {
        if (selectedImageUri != null) {
            if (converter_iamge_1 != null) {
                saveImage();
            }
        } else {
            Toast.makeText(requireContext(), "Selected image is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            saveImage();
        }
    }


    private void saveImage() {
        new Thread(() -> {
            try {
                int color_two = color1;
                dialogwindow customDialog = new dialogwindow();
                customDialog.show(getChildFragmentManager(), "CustomProgressDialog");
                customDialog.setCancelable(false);
                String imagename = String.valueOf(textInputLayout.getEditText().getText());

                String folderName = "Converterimage";

                File customFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
                if (!customFolder.exists()) {
                    customFolder.mkdirs();
                }
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                bitmap = two_change_alhpa(bitmap, color_two);
                bitmap = transparent(bitmap, tapancik);

                File file = new File(customFolder, imagename + converter_iamge_1);
                FileOutputStream outputStream = new FileOutputStream(file);

                bitmap.compress(converter_iamge_2, vorak, outputStream);
                if(save_cloud){
                    uploadAndSaveImage(bitmap, customDialog);
                }
                outputStream.flush();
                outputStream.close();
                MediaScannerConnection.scanFile(requireContext(), new String[]{file.getAbsolutePath()}, null, null);

                requireActivity().runOnUiThread(() -> {
                    customDialog.setCancelable(false);
                    try {

                        Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }).start();

    }

    private void uploadAndSaveImage(Bitmap bitmap, dialogwindow customDialog) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && bitmap != null) {

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            String imagenamefirebase = textInputLayout.getEditText().getText() + String.valueOf(System.currentTimeMillis());
            String imageId = UUID.randomUUID().toString();
            StorageReference imagesRef = storageRef.child("images/" + user.getUid() + "/" + imageId + "|" + imagenamefirebase);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(converter_iamge_2, vorak, baos);
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
                                    customDialog.dismiss();
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to retrieve download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to upload image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    customDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getActivity(), "User or image is null.", Toast.LENGTH_SHORT).show();
            customDialog.dismiss();
        }
    }




    private Bitmap tree_changealpha_to_color(Bitmap bitmap, int color) {
        if (selectedImageUri != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            for (int i = 0; i < pixels.length; i++) {
                int alpha = Color.alpha(pixels[i]);
                if (alpha == 0) {
                    pixels[i] = color;
                }
            }

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);





        }

        return bitmap;


    }





    private Bitmap two_change_alhpa(Bitmap bitmap, int color){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int[] pixels = new int[width * height];
        newBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            int alpha = Color.alpha(pixels[i]);
            if (alpha == 0) {
                pixels[i] = color;
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }



    private Bitmap transparent(Bitmap bitmap, int alpha) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAlpha(alpha);

        canvas.drawBitmap(bitmap, 0, 0, paint);

        return newBitmap;
    }



    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}

