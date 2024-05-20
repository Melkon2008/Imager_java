package com.imager.edit_it.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imager.edit_it.R;
import com.imager.edit_it.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final int MANAGE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    private Button openConverterimage, openMargeConvert, openContrastimage, openCloud;
    private TextView txt_home;
    private FragmentHomeBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference firebaseDatabase;

    private Runnable permissionsGrantedRunnable;

    private final ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allPermissionsGranted = true;
                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    if (!entry.getValue()) {
                        allPermissionsGranted = false;
                        break;
                    }
                }

                if (allPermissionsGranted && permissionsGrantedRunnable != null) {
                    permissionsGrantedRunnable.run();
                    permissionsGrantedRunnable = null;
                }
            });

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.slide_out)
                .build();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://edit-it-v2-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        openConverterimage = root.findViewById(R.id.openConvertImage);
        openMargeConvert = root.findViewById(R.id.openMargeFiles);
        openContrastimage = root.findViewById(R.id.openContrastImage);
        openCloud = root.findViewById(R.id.Cloud_save);
        txt_home = root.findViewById(R.id.txt_home);

        openCloud.setOnClickListener(v -> {
            requestPermissions(() -> {
                if (isInternetAvailable(getContext())) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
                    String savedEmail = preferences.getString("email", null);
                    String savedPassword = preferences.getString("password", null);
                    if (savedEmail != null && savedPassword != null) {
                        Navigation.findNavController(v)
                                .navigate(R.id.navigation_cloud_save, null, navOptions);
                    } else {
                        Navigation.findNavController(v)
                                .navigate(R.id.navigation_cloud_Sign_up, null, navOptions);
                    }
                } else {
                    Log.d("Internet", "No internet connection");
                }
            });
        });

        openContrastimage.setOnClickListener(v -> {
            requestPermissions(() -> {
                if (isInternetAvailable(getContext())) {
                    Navigation.findNavController(v)
                            .navigate(R.id.navigation_contrast, null, navOptions);
                } else {
                    Log.d("Internet", "No internet connection");
                }
            });
        });

        openMargeConvert.setOnClickListener(v -> {
            requestPermissions(() -> {
                if (isInternetAvailable(getContext())) {
                    Navigation.findNavController(v)
                            .navigate(R.id.navigation_marge, null, navOptions);
                } else {
                    Log.d("Internet", "No internet connection");
                }
            });
        });

        openConverterimage.setOnClickListener(v -> {
            requestPermissions(() -> {
                if (isInternetAvailable(getContext())) {
                    Navigation.findNavController(v)
                            .navigate(R.id.navigation_converter, null, navOptions);
                } else {
                    Log.d("Internet", "No internet connection");
                }
            });
        });

        return root;
    }

    private void requestPermissions(Runnable onPermissionsGranted) {
        permissionsGrantedRunnable = onPermissionsGranted;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestManageExternalStoragePermission();
            } else {
                // Permission already granted
                onPermissionsGranted.run();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsLauncher.launch(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            });
        } else {
            onPermissionsGranted.run();
        }

        Log.d("Permissions", "Permissions requested");
    }

    private void requestManageExternalStoragePermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s", getActivity().getPackageName())));
            startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST_CODE);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANAGE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    if (permissionsGrantedRunnable != null) {
                        permissionsGrantedRunnable.run();
                        permissionsGrantedRunnable = null;
                    }
                } else {
                    Log.e("Permissions", "MANAGE_EXTERNAL_STORAGE permission denied");
                }
            }
        }
    }

    private boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}