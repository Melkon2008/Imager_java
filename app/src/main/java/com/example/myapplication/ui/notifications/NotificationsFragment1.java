/*package com.example.myapplication.ui.notifications;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NotificationsFragment1 extends Fragment {

    private static final int PICK_IMAGE = 1;
    ImageView imageView;
    Uri selectedImageUri1;
    LinearLayout zip_layout;
    Button button_zip_open;

    Button zip_button_verj;

    int k = 0;


    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = root.findViewById(R.id.zip_imageView);
        button_zip_open = root.findViewById(R.id.zip_openGalleryButton);
        zip_button_verj = root.findViewById(R.id.zip_button_choose_verj);
        RadioButton pdfRadioButton = root.findViewById(R.id.pdf_converter);
        RadioButton zipRadioButton = root.findViewById(R.id.zip_converter);
        pdfRadioButton.setOnClickListener(this::onRadioButtonClicked);
        zipRadioButton.setOnClickListener(this::onRadioButtonClicked);


        zip_button_verj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archive();
            }
        });

        button_zip_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

        });


        return root;
    }
    private void openGallery () {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri1 = data.getData();
            imageView.setImageURI(selectedImageUri1);



        }
    }
    // Java Code
    String[] colorArray = new String[]{"#f6e58d", "#ffbe76", "#ff7979",
            "#badc58", "#dff9fb", "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"};


    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }
    public void onRadioButtonClicked(View view) {
        RadioButton radioButton = (RadioButton) view;
        boolean checked = radioButton.isChecked();
        String text = radioButton.getText().toString();

        switch (text) {
            case "Zip":
                if (checked) {
                    k = 1;
                }
                break;


            case "Pdf":
                if(checked){
                    k = 2;

                }
        }
    }

    public void archive(){
        if(k == 1){
            archiveImage(selectedImageUri1);
        }
        if(k == 2){
            createPdf();
        }


    }
    private void archiveImage(Uri imageUri) {

        if(imageUri != null){
            String folderName = "Ziparchive";
            String zipName = "zip_file" + System.currentTimeMillis() + ".zip";

            File zipFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), folderName);
            if (!zipFolder.exists()) {
                zipFolder.mkdirs();
            }
            File zipFile = new File(zipFolder, zipName);
            String imagePath = getRealPathFromURI(imageUri);

            if (imagePath != null) {
                String[] filePaths = {imagePath};
                String zipFilePath = String.valueOf(zipFile);

                ZipHelper zipHelper = new ZipHelper();
                zipHelper.createZipArchive(filePaths, zipFilePath);
                Toast.makeText(requireContext(), "Zip saved " + zipFilePath, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error ", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void createPdf() {
        if(selectedImageUri1 != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri1);
                if (bitmap != null) {
                    String folderName = "ConverterPdf";
                    String pdfName = "image_to_pdf_" + System.currentTimeMillis() + ".pdf";

                    File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), folderName);
                    if (!pdfFolder.exists()) {
                        pdfFolder.mkdirs();
                    }

                    File pdfFile = new File(pdfFolder, pdfName);

                    FileOutputStream outputStream = new FileOutputStream(pdfFile);

                    PdfDocument pdfDocument = new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.parseColor("#ffffff"));
                    canvas.drawPaint(paint);

                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    paint.setColor(Color.BLUE);
                    canvas.drawBitmap(bitmap, 0, 0, null);

                    pdfDocument.finishPage(page);
                    pdfDocument.writeTo(outputStream);
                    pdfDocument.close();

                    Toast.makeText(requireContext(), "PDF saved: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} */