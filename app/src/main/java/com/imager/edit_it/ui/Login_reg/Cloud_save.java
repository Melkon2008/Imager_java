package com.imager.edit_it.ui.Login_reg;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imager.edit_it.R;
import com.imager.edit_it.databinding.FragmentCloudSaveBinding;
import com.imager.edit_it.databinding.FragmentHomeBinding;


public class Cloud_save extends Fragment {

    FragmentCloudSaveBinding binding;



    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCloudSaveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}