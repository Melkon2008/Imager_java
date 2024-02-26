package com.example.myapplication.ui.FileMarge;


public class ItemModel {
    private String Name;
    private String Detail;



    public ItemModel(String name, String detail) {
        Name = name;
        Detail = detail;

    }

    public String getName() {
        return Name;
    }

    public String getDetail() {
        return Detail;
    }

}