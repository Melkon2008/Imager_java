package com.imager.edit_it.ui.Login_reg;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.imager.edit_it.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> imagePaths;
    private OnImageClickListener onImageClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);

        // Set dynamic width and height for ImageView
        setDynamicImageSize(holder.imageView);

        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.res_dialogwindow)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(imagePath);
            }
        });
    }

    private void setDynamicImageSize(ImageView imageView) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int itemSize = screenWidth / 4; // Divide by number of columns

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = itemSize;
        layoutParams.height = itemSize; // Making the height same as width to keep it square
        imageView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.idIVImage);
        }
    }

    public interface OnImageClickListener {
        void onImageClick(String imagePath);
    }
}
