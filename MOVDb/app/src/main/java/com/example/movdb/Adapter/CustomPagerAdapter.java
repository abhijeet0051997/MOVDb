package com.example.movdb.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.movdb.Framnets.OverviewFragmnet;
import com.example.movdb.Framnets.SimilarFragment;

public class CustomPagerAdapter extends FragmentPagerAdapter {
    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new OverviewFragmnet();
            case 1:
                return new SimilarFragment();

            default:
                return new OverviewFragmnet();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Overview";
            case 1:
                return "Similar Movies";
            default:
                return null;
        }
    }
}