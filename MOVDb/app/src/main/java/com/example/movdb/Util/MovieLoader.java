package com.example.movdb.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.movdb.POJO.Movie;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    String query;
    int mode;
    public MovieLoader(@NonNull Context context,String query,int mode) {
        super(context);
        this.query = query;
        this.mode = mode;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        URL url;
        List<Movie> movieData = new ArrayList<>();
        if(mode == 1)
        {
            url = NetworkUtil.buildSearchQuery(query);
            try {
                String jsonResponse = NetworkUtil.getResponseFromHttpUrl(url);
                movieData = NetworkUtil.extractMovie(jsonResponse);
            }catch (Exception e){
                e.printStackTrace();
                return movieData;
            }
        }else if(mode == 0){
            url = NetworkUtil.buildUrl(query);
            try {
                String jsonResponse = NetworkUtil.getResponseFromHttpUrl(url);
                movieData = NetworkUtil.extractMovie(jsonResponse);
            }catch (Exception e){
                e.printStackTrace();
                return movieData;
            }
        }
        else if(mode == 2)
        {
            url = NetworkUtil.buildSimilarUrl(query);
            try {
                String jsonResponse = NetworkUtil.getResponseFromHttpUrl(url);
                movieData = NetworkUtil.extractMovie(jsonResponse);
            }catch (Exception e){
                e.printStackTrace();
                return movieData;
            }
        }

        return movieData;
    }
}
