package com.imager.edit_it.ui.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imager.edit_it.R;
import com.imager.edit_it.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private Button openConverterimage, openMargeConvert, openContrastimage, openCloud, openmyaccount, opensettings, openhome;
    private TextView txt_home;
    private FragmentHomeBinding binding;

    private FirebaseAuth mAuth;

    private DatabaseReference firebaseDatabase;



    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        BottomNavigationView bottomNavigationView = root.findViewById(R.id.nav_view);


        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.slide_out)
                .build();


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://edit-it-v2-default-rtdb.europe-west1.firebasedatabase.app").getReference();






        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null) {
                    int currentId = currentDestination.getId();
                    if (itemId == R.id.navigation_my_account_v2 && currentId != R.id.navigation_my_account) {
                        navController.navigate(R.id.navigation_my_account, null, navOptions);

                        return true;
                    }
                    if (itemId == R.id.navigation_home_v2 && currentId != R.id.navigation_home) {
                        navController.navigate(R.id.navigation_home, null, navOptions);

                        return true;
                    }
                    if (itemId == R.id.navigation_settings_v2 && currentId != R.id.navigation_settings) {
                        navController.navigate(R.id.navigation_settings, null, navOptions);

                        return true;
                    }
                }

                return false;
            }
        });





        openConverterimage = root.findViewById(R.id.openConvertImage);
        openMargeConvert = root.findViewById(R.id.openMargeFiles);
        openContrastimage = root.findViewById(R.id.openContrastImage);
        openCloud = root.findViewById(R.id.Cloud_save);
        txt_home = root.findViewById(R.id.txt_home);

        openCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
                String savedEmail = preferences.getString("email", null);
                String savedPassword = preferences.getString("password", null);
                if (savedEmail != null && savedPassword != null) {
                    Navigation.findNavController(v)
                            .navigate(R.id.navigation_cloud_save, null, navOptions);


                }
                else{
                    Navigation.findNavController(v)
                            .navigate(R.id.navigation_cloud_Sign_up, null, navOptions);


                }



            }
        });

        openContrastimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                        .navigate(R.id.navigation_contrast, null, navOptions);
            }
        });

        openMargeConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                        .navigate(R.id.navigation_marge, null, navOptions);
            }
        });

        openConverterimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                        .navigate(R.id.navigation_converter, null, navOptions);
            }
        });


        return root;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
