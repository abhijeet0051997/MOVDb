package com.example.movdb.Util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.example.movdb.POJO.Movie;
import com.example.movdb.POJO.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class NetworkUtil {
    final static  String BASE_POSTER_URL="https://image.tmdb.org/t/p/w600_and_h900_bestv2";
    final static String API_BASE_URL =
            "https://api.themoviedb.org/3/movie/";
    final static String API_BASE_SEARCH_URL =
            "https://api.themoviedb.org/3/search/movie";

    final static String PARAM_KEY = "api_key";
    final static String API_KEY = "fc0887a1cae537e6a86c7d2537e8d8d5";
    final static String PARAM_LANGUAGE = "language";
    final static String language = "en-US";
    final static String QUERY = "query";

//    final static String PARAM_PAGE = "page";
//    final static String page = "1";

    public static URL buildSimilarUrl(String query) {

        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(query)
                .appendPath("similar")
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildSearchQuery(String query) {

        Uri builtUri = Uri.parse(API_BASE_SEARCH_URL).buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, language)
                .appendQueryParameter(QUERY, query)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



        public static URL buildUrl(String query) {

        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, language)

                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



    public static List<Movie> extractMovie(String jsonResponse) {
        if(jsonResponse.isEmpty()){
            return null;
        }

        List<Movie> movie = new ArrayList<>();
        try {
            JSONObject jsonrootobject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonrootobject.optJSONArray("results");
            if (jsonArray != null)
            {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.optJSONObject(i);

                    String title = obj.optString("title");
                    String url = obj.optString("poster_path");
                    int id = obj.optInt("id");
                    String date = obj.optString("release_date");
                    String poster_path = BASE_POSTER_URL+url;
                    SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date oldDate = oldDateFormat.parse(date);
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy");
                    String newDate = newDateFormat.format(oldDate);

                    Movie m = new Movie(title,poster_path,newDate,id);
                    movie.add(m);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "Problem parsing the Movie JSON results", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return movie;
    }

    public static MovieDetails extractMovieDetails(String jsonResponse) {
        if(jsonResponse.isEmpty()){
            return null;
        }
        MovieDetails m = new MovieDetails();
        try {
            JSONObject jsonrootobject = new JSONObject(jsonResponse);

            String title = jsonrootobject.optString("title");
            String overview = jsonrootobject.optString("overview");
            Double rate = jsonrootobject.optDouble("vote_average");
            String date = jsonrootobject.optString("release_date");
            SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date oldDate = oldDateFormat.parse(date);
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy");
            String newDate = newDateFormat.format(oldDate);
            MovieDetails movie = new MovieDetails(title,newDate,String.valueOf(rate),overview);
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();

            Log.e("QueryUtils", "Problem parsing the Movie JSON results", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    return m;

    }
}

