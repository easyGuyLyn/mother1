package com.dawoo.chessbox.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.chessbox.R;

import java.util.ArrayList;
import java.util.List;

public class MessageFragementAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragments = new ArrayList<>();
    List<String> mTitles = new ArrayList<>();
    Context mcontext;

    public MessageFragementAdapter(Context context ,FragmentManager fm, List fragments, List titles) {
        super(fm);
        mcontext = context;
        mFragments = fragments;
        mTitles = titles;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.layout_fragment_home_content_one_tab_layot_item_custom_view, null);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_tab);
        tvTitle.setText(mTitles.get(position));
        ImageView imgTab = (ImageView) v.findViewById(R.id.img_tab);
        return v;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }
}
