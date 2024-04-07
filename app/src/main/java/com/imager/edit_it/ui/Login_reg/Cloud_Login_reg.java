package com.imager.edit_it.ui.Login_reg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.imager.edit_it.databinding.LoginBinding;


public class Cloud_Login_reg extends Fragment {

    private LoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }



}
