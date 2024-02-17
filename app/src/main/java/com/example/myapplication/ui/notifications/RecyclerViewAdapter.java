package com.example.myapplication.ui.notifications;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewModel>
        implements RecyclerRowMoveCallback.RecyclerViewRowTouchHelperContract{

    private List<ItemModel> dataList;
    private List<Uri> arrayUri;

    private String pagecount;

    public int position2;





    Margefiles notificationsFragment = new Margefiles();

    public void setDataList(List<ItemModel> dataList){
        this.dataList =  dataList;
    }
    public void setArrayUri(List<Uri> arrayUri){
        this.arrayUri =  arrayUri;
    }







    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);

        return new MyViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, int position) {

        holder.lblItemName.setText(dataList.get(position).getName());
        holder.lblItemDetails.setText(dataList.get(position).getDetail());


        /*holder.btn_open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position2 = holder.getAdapterPosition();
                int pages = notificationsFragment.pdfPages2(position2);



                LinearLayout layaut_open_close = holder.itemView.findViewById(R.id.layaut_open_close);
                if (layaut_open_close.getVisibility() == View.VISIBLE) {
                    layaut_open_close.setVisibility(View.GONE);
                    layaut_open_close.removeAllViews();

                } else {
                    layaut_open_close.setVisibility(View.VISIBLE);
                    for(int i = 1; i <= pages; i++){
                        TextView textView = new TextView(holder.layaut_open_close.getContext());
                        textView.setText("Pdf page" + i);
                        int leftPaddingPx = 20;
                        int topPaddingPx = 10;
                        int rightPaddingPx = 20;
                        int bottomPaddingPx = 10;
                        textView.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
                        holder.layaut_open_close.addView(textView);
                    }
                }
            }
        }); */

    }





    @Override
    public int getItemCount() {
        return dataList.size();
    }




    @Override
    public void onRowMoved(int from, int to) {


        if(from < to)
        {
            for(int i=from; i<to; i++)
            {
                Collections.swap(dataList,i,i+1);
                Collections.swap(arrayUri,i,i+1);
            }
        }
        else
        {
            for(int i=from; i>to; i--)
            {
                Collections.swap(dataList,i,i-1);
                Collections.swap(arrayUri,i,i-1);
            }
        }
        notifyItemMoved(from,to);
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

        TextView lblItemName, lblItemDetails, makettext;


        Button btn_open_close;

        LinearLayout layaut_open_close;

        LinearLayout linear_maketext;

        CardView cardView;

        public MyViewModel(@NonNull View itemView) {

            super(itemView);
            layaut_open_close = itemView.findViewById(R.id.layaut_open_close);
            btn_open_close = itemView.findViewById(R.id.btn_open_close);
            lblItemName = itemView.findViewById(R.id.lblItemName);
            lblItemDetails = itemView.findViewById(R.id.lblItemDetails);
            cardView = itemView.findViewById(R.id.cardView);


        }

    }
}
