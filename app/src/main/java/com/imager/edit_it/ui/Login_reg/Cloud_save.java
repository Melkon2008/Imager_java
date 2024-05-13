package com.imager.edit_it.ui.Login_reg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.imager.edit_it.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Cloud_save extends Fragment {

    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud_save, container, false);

        // Initialize views
        imagesRV = rootView.findViewById(R.id.idRVImages);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        // Prepare recycler view
        prepareRecyclerView();

        // Load images from Firebase Storage
        loadImagesFromFirebase();

        // Setup swipe to refresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> loadImagesFromFirebase());

        return rootView;
    }

    private void prepareRecyclerView() {
        imagePaths = new ArrayList<>();

        imageRVAdapter = new RecyclerViewAdapter(context, imagePaths);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);

        imageRVAdapter.setOnImageClickListener(imagePath -> openImageDetailActivity(imagePath));
    }

    private static final int REQUEST_IMAGE_DETAIL = 1;

    private void openImageDetailActivity(String imagePath) {
        Intent intent = new Intent(getContext(), ImageDetailActivity.class);
        intent.putExtra("imgPath", imagePath);
        startActivityForResult(intent, REQUEST_IMAGE_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_DETAIL && resultCode == Activity.RESULT_OK) {
            loadImagesFromFirebase();
        }
    }

    private void loadImagesFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }



        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(currentUser.getUid());

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    List<StorageReference> items = listResult.getItems();

                    List<Pair<StorageReference, Uri>> imageUris = new ArrayList<>();

                    AtomicInteger count = new AtomicInteger(items.size());

                    for (StorageReference item : items) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUris.add(new Pair<>(item, uri));
                            if (count.decrementAndGet() == 0) {
                                handleImageUris(imageUris);
                            }
                        }).addOnFailureListener(e -> {
                            count.decrementAndGet();
                            if (count.get() == 0) {
                                handleImageUris(imageUris);
                            }
                            Toast.makeText(context, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, "Failed to list images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void handleImageUris(List<Pair<StorageReference, Uri>> imageUris) {

        List<Pair<Uri, Long>> imageTimeStamps = new ArrayList<>();


        for (Pair<StorageReference, Uri> pair : imageUris) {
            Uri uri = pair.second;
            StorageReference storageRef = pair.first;
            storageRef.getMetadata().addOnSuccessListener(metadata -> {
                long creationTimeMillis = metadata.getCreationTimeMillis();
                imageTimeStamps.add(new Pair<>(uri, creationTimeMillis));
                if (imageTimeStamps.size() == imageUris.size()) {
                    Collections.sort(imageTimeStamps, (pair1, pair2) -> pair2.second.compareTo(pair1.second));
                    imagePaths.clear();
                    for (Pair<Uri, Long> timeStampPair : imageTimeStamps) {
                        imagePaths.add(timeStampPair.first.toString());
                    }

                    imageRVAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Failed to load image metadata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            });
        }
    }

}