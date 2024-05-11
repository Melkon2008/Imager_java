package com.imager.edit_it.ui.home.home_navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.imager.edit_it.R;
import com.imager.edit_it.databinding.MyAccountBinding;


public class Home_My_account extends Fragment {

    private MyAccountBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MyAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

}
