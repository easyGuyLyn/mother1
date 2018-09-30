package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.view.fragment.BaseFragment;

/**
 * 福利支取
 */
public class WelfareDrawFragment extends BaseFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_welfare_draw_fragment, container, false);
        return view;
    }

    @Override
    protected void loadData() {

    }
}
