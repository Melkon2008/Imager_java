package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.myapplication.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();

        setContentView(binding.getRoot());



    }


}







