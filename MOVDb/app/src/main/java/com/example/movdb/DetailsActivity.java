package com.example.movdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.movdb.Adapter.CustomPagerAdapter;

public class DetailsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    CustomPagerAdapter adapter;
    public static final String MyPREFERENCES = "MyPrefs" ;
    ImageView posterView;
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout= findViewById(R.id.collapsingtoolbar);
        tabLayout =(TabLayout)findViewById(R.id.tab_layout);
        pager =(ViewPager)findViewById(R.id.viewPager);
        adapter = new CustomPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        posterView=(ImageView)findViewById(R.id.movie_poster);
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES , MODE_PRIVATE);
        String poster = prefs.getString("MOVIE_POSTER",null);
        String title = prefs.getString("MOVIE_TITLE",null);
        toolbar.setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        String base = "https://image.tmdb.org/t/p/w500/";
        String [] segment = poster.split("/");
        String movPoster = base + segment[segment.length -1];
        Log.d("message",movPoster);
        Glide.with(getApplicationContext())
                .load(movPoster)
                .placeholder(R.drawable.placeholder)
                .into(posterView);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
