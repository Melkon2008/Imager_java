package com.imager.edit_it.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.imager.edit_it.R;
import com.imager.edit_it.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private Button openConverterimage, openMargeConvert, openContrastimage;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.slide_out)
                .build();

        openConverterimage = root.findViewById(R.id.openConvertImage);
        openMargeConvert = root.findViewById(R.id.openMargeFiles);
        openContrastimage = root.findViewById(R.id.openContrastImage);

        openContrastimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(R.id.navigation_contrast, null, navOptions);
            }
        });
        openMargeConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                        .navigate(R.id.navigation_marge, null, navOptions);
            }
        });
        openConverterimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
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
