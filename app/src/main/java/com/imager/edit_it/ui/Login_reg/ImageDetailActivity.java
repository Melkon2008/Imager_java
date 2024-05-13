// ImageDetailActivity.java
package com.imager.edit_it.ui.Login_reg;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imager.edit_it.R;

public class ImageDetailActivity extends AppCompatActivity {

    private String imgPath;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private Button btnDelete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imgPath = getIntent().getStringExtra("imgPath");

        imageView = findViewById(R.id.idIVImage);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> deleteImage());


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Glide.with(this)
                .load(imgPath)
                .placeholder(R.drawable.res_dialogwindow)
                .into(imageView);
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
