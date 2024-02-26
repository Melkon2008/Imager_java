package com.example.myapplication.ui.ContrastImage;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ContrastImageBinding;

import java.io.IOException;

public class Contrastimage extends Fragment {

    private Button openContrastImage;
    private ContrastImageBinding binding;

    private ImageView imageView;
    private static final int PICK_IMAGE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ContrastImageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        openContrastImage = root.findViewById(R.id.btncontrastimage2);
        imageView = root.findViewById(R.id.contrast_image_2);
        openContrastImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return root;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                Bitmap contrastBitmap = increaseContrast(bitmap);
                imageView.setImageBitmap(contrastBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static Bitmap increaseContrast(Bitmap bitmap) {
        // Создаем матрицу для увеличения контраста
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        // Применяем матрицу к изображению
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(newBitmap);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return newBitmap;
    }
}
