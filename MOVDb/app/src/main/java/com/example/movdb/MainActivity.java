package com.example.movdb;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.movdb.Adapter.MovieRecyclerAdapter;
import com.example.movdb.POJO.Movie;
import com.example.movdb.Util.MovieLoader;
import com.example.movdb.Util.NetworkUtil;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.movdb.Framnets.OverviewFragmnet.MyPREFERENCES;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieRecyclerAdapter.onMovieItemClickListener {

    private Toolbar toolbar;
    private Spinner spinner;
    private RecyclerView moviesView;
    private MovieRecyclerAdapter movieAdapter;
    List<Movie> movies;
    SimpleCursorAdapter mAdapter;
    String query="";
    int mode = 0;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        moviesView =(RecyclerView)findViewById(R.id.movies_recycler);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        final String[] spinnerOptions = new String[] {
                "Now Playing", "Popular", "Top Rated", "Upcoming" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinnerOptions);
        spinner.setAdapter(adapter);
        movies = new ArrayList<>();
        movieAdapter = new MovieRecyclerAdapter(this,movies,this);
        moviesView.setLayoutManager(new GridLayoutManager(this,3));
        moviesView.setAdapter(movieAdapter);
        handleIntent(getIntent());


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPos(position);
                getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getSupportLoaderManager().initLoader(1, null,MainActivity.this);


        final String[] from = new String[] {"data"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    private void checkPos(int x) {

        switch (x)
        {
            case 0:
                query = "now_playing";
                mode = 0;
                break;
            case 1:
                query = "popular";
                mode = 0;
                break;
            case 2:
                query = "top_rated";
                mode = 0;
                break;
            case 3:
                query = "upcoming";
                mode = 0;
                break;
            default:
                query = "now_playing";
                mode = 0;
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu,menu);
        final MenuItem menuSearch = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {

                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex("data"));
                searchView.setQuery(txt, true);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    populateAdapter(s);
                return false;
            }
        });

        return true;
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new MovieLoader(this,query,mode);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        if(!movies.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            movieAdapter.setData(movies);
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        movieAdapter.setData(new ArrayList<Movie>());
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String q = intent.getStringExtra(SearchManager.QUERY);
            query =q;
            mode = 1;
            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }
    private void populateAdapter(String query) {

            SearchTask task = new SearchTask();
            task.execute(query);
    }

    @Override
    public void onMovieClicked(int id,String poster,String title) {
        Intent i = new Intent(MainActivity.this,DetailsActivity.class);
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("MOVIE_ID", String.valueOf(id));
        editor.putString("MOVIE_POSTER",poster);
        editor.putString("MOVIE_TITLE",title);
        editor.commit();
        startActivity(i);
    }

    class SearchTask extends AsyncTask<String,Void,List<Movie>>{

        @Override
        protected List<Movie> doInBackground(String... strings) {
            String query = strings[0];
            List<Movie> movie= new ArrayList<Movie>();
            URL url = NetworkUtil.buildSearchQuery(query);

            try{
                String jsonResponse = NetworkUtil.getResponseFromHttpUrl(url);
                movie = NetworkUtil.extractMovie(jsonResponse);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return movie;
            
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "data" });
          if(movies.size() <=5)
            {
                for (int i=0; i< movies.size(); i++) {
                    c.addRow(new Object[] {i, movies.get(i).getMovie_title()});

                }
                mAdapter.changeCursor(c);
            }
            else if(!movies.isEmpty())
            {
                for (int i=0; i< 5; i++) {
                    c.addRow(new Object[] {i, movies.get(i).getMovie_title()});

                }
                mAdapter.changeCursor(c);
            }

        }
    }

}
