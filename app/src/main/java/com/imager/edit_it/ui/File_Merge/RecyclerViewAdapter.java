package com.imager.edit_it.ui.File_Merge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.imager.edit_it.R;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewModel>
        implements RecyclerRowMoveCallback.RecyclerViewRowTouchHelperContract {

    private List<ItemModel> dataList;
    private List<Uri> arrayUri;
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<ItemModel> dataList) {
        this.dataList = dataList;
    }

    public void setArrayUri(List<Uri> arrayUri) {
        this.arrayUri = arrayUri;
    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new MyViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, int position) {
        holder.lblItemName.setText(dataList.get(position).getName());
        holder.lblItemDetails.setText(dataList.get(position).getDetail());

        holder.openPdfFile.setOnClickListener(v -> {
            Uri pdfUri = arrayUri.get(position);
            Intent intent = PdfViewerActivity.Companion.launchPdfFromPath(
                    context,
                    pdfUri.toString(),
                    dataList.get(position).getName(),
                    saveTo.ASK_EVERYTIME,
                    false
            );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onRowMoved(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(dataList, i, i + 1);
                Collections.swap(arrayUri, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(dataList, i, i - 1);
                Collections.swap(arrayUri, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
    }

    @Override
    public void onRowSelected(MyViewModel myViewHolder) {
        myViewHolder.cardView.setCardBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(MyViewModel myViewHolder) {
        myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#12dddd"));
    }

    public void deleteItem(int position) {
        dataList.remove(position);
        arrayUri.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewModel extends RecyclerView.ViewHolder {
        TextView lblItemName, lblItemDetails;
        ImageView openPdfFile;

        CardView cardView;

        public MyViewModel(@NonNull View itemView) {
            super(itemView);

            lblItemName = itemView.findViewById(R.id.lblItemName);
            lblItemDetails = itemView.findViewById(R.id.lblItemDetails);
            cardView = itemView.findViewById(R.id.cardView);
            openPdfFile = itemView.findViewById(R.id.Viewpdffile);
        }
    }
}
