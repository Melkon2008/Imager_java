package com.imager.edit_it.ui.Split_pdf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
public class Split_pdf extends Fragment {

    private static final int PICK_PDF_REQUEST = 1;

    TextInputLayout textInputLayout;

    private Button openPdfImage, plus_split, btn_save_split_pdf;

    TextView itemname, itempages;

    Uri PDFselected;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<ItemModel> itemList = new ArrayList<>();
    private static final int PICK_IMAGE = 1;


    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.split_pdf, container, false);

        openPdfImage = root.findViewById(R.id.btncontrastimage2);
        recyclerView = root.findViewById(R.id.recyclerView);
        plus_split = root.findViewById(R.id.plus_split);
        itemname = root.findViewById(R.id.lblItemName);
        itempages = root.findViewById(R.id.lblItemDetails);
        btn_save_split_pdf = root.findViewById(R.id.save_split_pdf);
        textInputLayout = root.findViewById(R.id.father_file_name_refractor);


        btn_save_split_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemList.size() != 0 && PDFselected != null){
                    savePDFFiles();
                }
                else{
                    Toast.makeText(requireContext(), "Pick File", Toast.LENGTH_LONG).show();
                }


            }
        });

        plus_split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemModel newItem = new ItemModel("", "");
                itemList.add(newItem);
                adapter.notifyItemInserted(itemList.size() - 1);
            }
        });

        openPdfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPdfFile();

            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                itemList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);



        adapter = new Adapter(requireContext(), itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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
                PDFselected = data.getData();
                String imagename = getpdfname(PDFselected);
                itempages.setText(pdfPages(PDFselected));
                itemname.setText(imagename);
                textInputLayout.getEditText().setText(imagename);


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
    private String getpdfname(Uri uri) {
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


    private void savePDFFiles() {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_PDF_REQUEST);
            }
            else {
                stugelpdf();
                /*List<ItemModel> allItems1 = adapter.getAllItems();
                if (allItems1.size() == 1) {
                    splitAndSavePdf_2();
                }
                else{
                    splitAndSavePdf();
                }*/
            }
        }

    private void stugelpdf(){
        List<ItemModel> allItems2 = adapter.getAllItems();
        int pdf_pages_start_2 = 0;
        int pdf_pages_end_2 = 0;
        ItemModel itemModel_2 = allItems2.get(0);
        StringBuilder pdfv1_1_2 = new StringBuilder();
        StringBuilder pdfv1_2_2 = new StringBuilder();

        pdfv1_1_2.append(itemModel_2.getItemName());
        pdfv1_2_2.append(itemModel_2.getItemDetails());
        String pd1_2 = pdfv1_1_2.toString();
        String pd2_2 = pdfv1_2_2.toString();
        if (!(pd1_2.isEmpty()) && !(pd2_2.isEmpty())) {
            pdf_pages_start_2 = Integer.valueOf(String.valueOf(pdfv1_1_2));
            pdf_pages_end_2 = Integer.valueOf(String.valueOf(pdfv1_2_2));
        }

        if ((!(pd1_2.isEmpty()) || !(pd2_2.isEmpty())) && (pdf_pages_start_2 < pdf_pages_end_2) && (pdf_pages_start_2 > 0)) {
            splitAndSavePdf();
        }


    }

    private  void splitAndSavePdf_2() {
        List<ItemModel> allItems1 = adapter.getAllItems();
            int pdf_pages_start = 0;
            int pdf_pages_end = 0;
            ItemModel itemModel = allItems1.get(0);
            StringBuilder pdfv1_1 = new StringBuilder();
            StringBuilder pdfv1_2 = new StringBuilder();

            pdfv1_1.append(itemModel.getItemName());
            pdfv1_2.append(itemModel.getItemDetails());
            String pd1 = pdfv1_1.toString();
            String pd2 = pdfv1_2.toString();
        if (!(pd1.isEmpty()) && !(pd2.isEmpty())) {
            pdf_pages_start = Integer.valueOf(String.valueOf(pdfv1_1));
            pdf_pages_end = Integer.valueOf(String.valueOf(pdfv1_2));
        }

            if ((!(pd1.isEmpty()) || !(pd2.isEmpty())) && (pdf_pages_start < pdf_pages_end) && (pdf_pages_start > 0)) {
                String imageName = String.valueOf(textInputLayout.getEditText().getText());
                File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDfmarge");
                File outputFile = new File(outputDir, imageName);

                PdfWriter pdfWriter = null;
                try {
                    pdfWriter = new PdfWriter(new FileOutputStream(outputFile));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                PdfDocument mergedPdf = new PdfDocument(pdfWriter);
                Document document = new Document(mergedPdf);

                try (InputStream inputStream = requireContext().getContentResolver().openInputStream(PDFselected);
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                    PdfReader pdfReader1 = new PdfReader(bufferedInputStream);
                    pdfReader1.setUnethicalReading(true);
                    PdfDocument pdfDocument1 = new PdfDocument(pdfReader1);
                    inputStream.close();
                    int max_pages = pdfDocument1.getNumberOfPages();

                    pdfDocument1.copyPagesTo(pdf_pages_start, pdf_pages_end, mergedPdf);

                    mergedPdf.close();
                    pdfWriter.close();
                    document.close();
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Pdf document" + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }

            }

    }


        public void splitAndSavePdf() {
            String imageName = String.valueOf(textInputLayout.getEditText().getText());
            List<ItemModel> allItems = adapter.getAllItems();
            File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDfmarge");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, imageName);

            PdfWriter pdfWriter = null;
            try {
                pdfWriter = new PdfWriter(new FileOutputStream(outputFile));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            PdfDocument mergedPdf = new PdfDocument(pdfWriter);
            Document document = new Document(mergedPdf);

            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(PDFselected);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                PdfReader pdfReader1 = new PdfReader(bufferedInputStream);
                pdfReader1.setUnethicalReading(true);
                PdfDocument pdfDocument1 = new PdfDocument(pdfReader1);
                inputStream.close();
                int max_pages = pdfDocument1.getNumberOfPages();
                for (ItemModel item : allItems) {
                    StringBuilder pdf_1 = new StringBuilder();
                    StringBuilder pdf_2 = new StringBuilder();


                    pdf_1.append(item.getItemName());
                    pdf_2.append(item.getItemDetails());
                    String pd1 = pdf_1.toString();
                    String pd2 = pdf_2.toString();
                    int pdf_pages_start = 0;
                    int pdf_pages_end = 0;
                    if (!(pd1.isEmpty()) && !(pd2.isEmpty())) {
                        pdf_pages_start = Integer.valueOf(String.valueOf(pdf_1));
                        pdf_pages_end = Integer.valueOf(String.valueOf(pdf_2));
                    }

                    if ((!(pd1.isEmpty()) || !(pd2.isEmpty())) && (pdf_pages_start < pdf_pages_end) && (pdf_pages_start > 0 && pdf_pages_end <= max_pages)) {

                        pdfDocument1.copyPagesTo(pdf_pages_start, pdf_pages_end, mergedPdf);
                    }


                }

                mergedPdf.close();
                pdfWriter.close();
                document.close();
                itemList.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Pdf document" + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }


        }



}

