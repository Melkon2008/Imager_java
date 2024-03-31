package com.imager.edit_it.ui.File_Merge;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imager.edit_it.R;
import com.imager.edit_it.databinding.PdfmergeBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.itextpdf.io.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Margefiles extends Fragment {

    private static final int PICK_PDF_REQUEST = 1;
    private static final int REQUEST_CODE_MANAGE_STORAGE_PERMISSION = 123;
    private PdfmergeBinding binding;

    private int tiv_array;
    private List<Uri> listPDFUri = new ArrayList<>();
    private ArrayList<ItemModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private TextInputLayout textInputLayout;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PdfmergeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Button btnpdf = root.findViewById(R.id.btnPickPdf);
        Button btnsavepdf = root.findViewById(R.id.btnSavePdf);


        textInputLayout = root.findViewById(R.id.father_file_name_refractor);

        btnsavepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePDFFiles();
            }
        });

        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPdfFile();
            }
        });




        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter();
        adapter.setDataList(list);


        ItemTouchHelper.Callback callback = new RecyclerRowMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);


        return root;
    }

    private void pickPdfFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), PICK_PDF_REQUEST);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri PDFselected = data.getData();
                listPDFUri.add(PDFselected);
                tiv_array++;
                Toast.makeText(requireContext(), "Selected PDF File:" + tiv_array, Toast.LENGTH_LONG).show();

                String filename = getFileNameUri(PDFselected);



                list.add(new ItemModel(filename, pdfPages(PDFselected) + " pages"));

                recyclerView.getAdapter().notifyDataSetChanged();
                adapter.setArrayUri(listPDFUri);
                adapter.notifyDataSetChanged();
                textInputLayout.getEditText().setText("MargePdfdocument" + System.currentTimeMillis());

            }
        }
    }

    public String pdfPages(Uri uri) {

        int pageCount = 0;
        try {
            PdfReader reader = new PdfReader(requireContext().getContentResolver().openInputStream(uri));
            PdfDocument pdfDocument1 = new PdfDocument(reader);
            pageCount = pdfDocument1.getNumberOfPages();
            reader.close();
            pdfDocument1.close();


        } catch (IOException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);


        } catch (java.io.IOException e) {


            throw new RuntimeException(e);
        }
        return String.valueOf(pageCount);
    }



    @SuppressLint("Range")
    private String getFileNameUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex("_display_name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Boolean savePDFFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE_PERMISSION);
        } else {
            mergeAndSavePdf();
        }
        return null;
    }


    private void mergeAndSavePdf() {
        try {
            String imageName = String.valueOf(textInputLayout.getEditText().getText());
            adapter.setArrayUri(listPDFUri);
            adapter.notifyDataSetChanged();

            File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDfmarge");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, imageName + ".pdf");

            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(outputFile));
            PdfDocument mergedPdf = new PdfDocument(pdfWriter);
            Document document = new Document(mergedPdf);
            int listitems = 0;
            for (int i = 0; i < listPDFUri.size(); i++) {
                try (InputStream inputStream = requireContext().getContentResolver().openInputStream(listPDFUri.get(i));
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                    PdfReader pdfReader1 = new PdfReader(bufferedInputStream);
                    pdfReader1.setUnethicalReading(true);
                    PdfDocument pdfDocument1 = new PdfDocument(pdfReader1);
                    inputStream.close();
                    int max_pages = pdfDocument1.getNumberOfPages();
                    boolean pages_true_false = true;
                    int j1 = 1;
                    if(max_pages >= 10 && pages_true_false){
                        for(int j = 1; j <= max_pages; j+=10){
                            if((j - 10) < 10){
                                j1 = j;
                                pages_true_false = false;
                                break;
                            }
                            else{
                                pdfDocument1.copyPagesTo(j, j + 10, mergedPdf);
                            }
                        }
                    }
                    if(pages_true_false == false){
                            pdfDocument1.copyPagesTo(j1, max_pages, mergedPdf);
                            pages_true_false = true;
                    }
                    else if(max_pages < 10){
                        pdfDocument1.copyPagesTo(1, max_pages, mergedPdf);

                    }


                    //pdfDocument1.copyPagesTo(1, pdfDocument1.getNumberOfPages(), mergedPdf);
                    pdfDocument1.close();
                    pdfReader1.close();

                    listPDFUri.remove(listitems);
                    listitems++;




                } catch (IOException e) {
                    e.printStackTrace();
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }
            mergedPdf.close();
            pdfWriter.close();
            document.close();





            Toast.makeText(requireContext(), "Pdf document" + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            listPDFUri.clear();
            list.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
            tiv_array = 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

}
