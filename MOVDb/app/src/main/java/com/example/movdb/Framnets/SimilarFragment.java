package com.example.movdb.Framnets;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movdb.Adapter.MovieRecyclerAdapter;
import com.example.movdb.DetailsActivity;
import com.example.movdb.POJO.Movie;
import com.example.movdb.R;
import com.example.movdb.Util.MovieLoader;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimilarFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieRecyclerAdapter.onMovieItemClickListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public SimilarFragment() {
        // Required empty public constructor
    }

    RecyclerView similarRecycler;
    List<Movie> movies;
    MovieRecyclerAdapter adapter;
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_similar, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES , MODE_PRIVATE);
        id = prefs.getString("MOVIE_ID",null);
        similarRecycler =(RecyclerView)v.findViewById(R.id.similar_recycler);
        movies = new ArrayList<>();
        adapter = new MovieRecyclerAdapter(getActivity(),movies,this);
        similarRecycler.setLayoutManager(new GridLayoutManager(getActivity(),3));
        similarRecycler.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(1, null,this);
        return v;
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new MovieLoader(getActivity(),id,2);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        adapter.setData(movies);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        adapter.setData(new ArrayList<Movie>());
    }

    @Override
    public void onMovieClicked(int id, String poster,String title) {
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        SharedPreferences sharedpreferences;
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("MOVIE_ID", String.valueOf(id));
        editor.putString("MOVIE_POSTER",poster);
        editor.putString("MOVIE_TITLE",title);

        editor.commit();
        startActivity(i);
    }
}
