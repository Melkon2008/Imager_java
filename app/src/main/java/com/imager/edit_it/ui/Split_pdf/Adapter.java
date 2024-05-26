package com.imager.edit_it.ui.Split_pdf;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imager.edit_it.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private static List<ItemModel> itemList;
    private Context context;

    public Adapter(Context context, List<ItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maket_split, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.textInputLayout1.getEditText().setText(item.getItemName());
        holder.textInputLayout2.getEditText().setText(item.getItemDetails());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<ItemModel> getAllItems() {
        return itemList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextInputLayout textInputLayout1;
        public TextInputLayout textInputLayout2;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textInputLayout1 = itemView.findViewById(R.id.father_file_name_refractor);
            textInputLayout2 = itemView.findViewById(R.id.father_file_nadme_refractor);

            textInputLayout1.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ItemModel item = itemList.get(position);
                        item.setItemName(s.toString());
                    }
                }
            });

            textInputLayout2.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ItemModel item = itemList.get(position);
                        item.setItemDetails(s.toString());
                    }
                }
            });
        }
    }

}
