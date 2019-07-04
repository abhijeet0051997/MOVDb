package com.example.movdb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movdb.POJO.Movie;
import com.example.movdb.R;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieRecyclerViewHolder> {
    Context context;
    List<Movie> movies = new ArrayList<>();
    onMovieItemClickListener listener;

    public MovieRecyclerAdapter(Context context, List<Movie> movies,onMovieItemClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }
    public interface onMovieItemClickListener{
        void onMovieClicked(int id,String poster,String title);
    }

    @NonNull
    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.movies_item_layout,viewGroup,false);
        return new MovieRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerViewHolder movieRecyclerViewHolder, int i) {
        Glide.with(context)
                .load(movies.get(i).getMovie_poster())
                .placeholder(R.drawable.placeholder)
                .into(movieRecyclerViewHolder.posterImage);
        movieRecyclerViewHolder.movieTitle.setText(movies.get(i).getMovie_title());
        movieRecyclerViewHolder.movieYear.setText(movies.get(i).getMovie_year());
    }

    @Override
    public int getItemCount() {
            return movies.size();
    }

    class MovieRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterImage;
        TextView movieTitle;
        TextView movieYear;
        public MovieRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage =(ImageView)itemView.findViewById(R.id.movie_poster);
            movieTitle =(TextView)itemView.findViewById(R.id.movie_title);
            movieYear =(TextView)itemView.findViewById(R.id.movie_year);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onMovieClicked(movies.get(pos).getMovie_id(),movies.get(pos).getMovie_poster(),movies.get(pos).getMovie_title());
        }
    }
    public void setData (List <Movie > movieData) {
           movies = movieData;
           notifyDataSetChanged();
       }

}
