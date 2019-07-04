package com.example.movdb.Framnets;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movdb.POJO.MovieDetails;
import com.example.movdb.R;
import com.example.movdb.Util.NetworkUtil;

import java.io.IOException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragmnet extends Fragment {


    public OverviewFragmnet() {
        // Required empty public constructor
    }

    public static final String MyPREFERENCES = "MyPrefs" ;
    TextView title,year,rating,overview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overview_fragmnet, container, false);
        title =(TextView)v.findViewById(R.id.movie_title);
        rating =(TextView)v.findViewById(R.id.movie_rating);
        year =(TextView)v.findViewById(R.id.movie_year);
        overview =(TextView)v.findViewById(R.id.movie_overview);
        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES , MODE_PRIVATE);
        String restoredText = prefs.getString("MOVIE_ID", null);
        MovieDetailsTask task = new MovieDetailsTask();
        task.execute(restoredText);

        return v;
    }
    class MovieDetailsTask extends AsyncTask<String,Void, MovieDetails>
    {

        @Override
        protected MovieDetails doInBackground(String... strings) {
            String query =strings[0];
            URL url = NetworkUtil.buildUrl(query);
            MovieDetails m = new MovieDetails();
            try {
                String json = NetworkUtil.getResponseFromHttpUrl(url);
                MovieDetails m1 = NetworkUtil.extractMovieDetails(json);
            return m1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return m;
        }

        @Override
        protected void onPostExecute(MovieDetails movieDetails) {
            title.setText(movieDetails.getMovie_title());
            year.setText(movieDetails.getYear());
            rating.setText(movieDetails.getRating());
            overview.setText(movieDetails.getOverview());
        }
    }

}
