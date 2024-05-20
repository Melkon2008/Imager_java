package com.imager.edit_it.ui.Login_reg.Otstup;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EqualSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    public EqualSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int spanCount = ((RecyclerView.LayoutManager) parent.getLayoutManager()).getWidth() / view.getLayoutParams().width;
        int column = position % spanCount; // item column

        outRect.left = column * spacing / spanCount;
        outRect.right = spacing - (column + 1) * spacing / spanCount;
        if (position >= spanCount) {
            outRect.top = spacing; // item top
        }
    }
}
