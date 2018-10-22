package com.dawoo.chessbox.view.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.chessbox.BoxApplication;

/**
 * Created by archar on 18-2-9.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int spaceH;

    public SpaceItemDecoration(int space, int spaceH) {
        this.space = space;
        this.spaceH = spaceH;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = spaceH;
        outRect.right = spaceH;
        outRect.bottom = space;
        //注释这两行是为了上下间距相同
//        if(parent.getChildAdapterPosition(view)==0){
        outRect.top = space;
//        }
    }
}
