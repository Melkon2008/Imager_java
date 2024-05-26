package com.imager.edit_it.ui.Login_reg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.imager.edit_it.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private String imgPath;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private Button btnDelete, btnsave;


    TextView imagename;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imgPath = getIntent().getStringExtra("imgPath");

        imageView = findViewById(R.id.idIVImage);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        btnDelete = findViewById(R.id.btn_delete);
        btnsave = findViewById(R.id.btn_save);
        imagename = findViewById(R.id.Image_name_cloud);

        btnDelete.setOnClickListener(v -> deleteImage());




        btnsave.setOnClickListener(v -> imagesave());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Glide.with(this)
                .load(imgPath)
                .placeholder(R.drawable.res_dialogwindow)
                .into(imageView);



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imgPath);
        storageRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {

                String imageName = storageMetadata.getName();
                imagename.setText(getnameimage(imageName));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);

            return true;
        }
    }

    private String getnameimage(String path) {

        String imagename1;
        String[] parts = path.split("\\|");
        imagename1 = parts[1];
        return imagename1  ;
    }

    private void imagesave() {
        String folderName = "Converterimage";

        File customFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imgPath);
        storageRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String imgpath = storageMetadata.getContentType();
                String imagename = storageMetadata.getName();
                String[] parts = imgpath.split("/");
                String[] partsimagename = imagename.split("\\|");



                imgpath = parts[1];
                imagename = partsimagename[1];

                final File localFile;
                try {
                    localFile = File.createTempFile(imagename, imgpath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String finalImgpath = imgpath;

                String finalImagename = imagename;
                storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    File file = new File(customFolder, finalImagename + "." + finalImgpath);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        outputStream.flush();
                        outputStream.close();
                        MediaScannerConnection.scanFile(ImageDetailActivity.this, new String[]{file.getAbsolutePath()}, null, null);

                        Toast.makeText(ImageDetailActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {

                        Toast.makeText(ImageDetailActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }




    private void deleteImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imgPath);

        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ImageDetailActivity.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to delete the file
                Toast.makeText(ImageDetailActivity.this, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}