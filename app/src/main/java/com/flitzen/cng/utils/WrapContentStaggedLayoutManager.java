package com.flitzen.cng.utils;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class WrapContentStaggedLayoutManager extends StaggeredGridLayoutManager {

    public WrapContentStaggedLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    //... constructor
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
        }
    }
}
