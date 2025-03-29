package com.example.moviesgo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MovieViewHolder> {

    private final List<MovieResult> movieResults;
    private final Context context;

    public MovieSearchAdapter(List<MovieResult> movieResults, Context context) {
        this.movieResults = movieResults;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_container_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindItem(movieResults.get(position), context);
    }

    @Override
    public int getItemCount() {
        return movieResults.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView imageItemPoster;
        private final TextView textItemName;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageItemPoster = itemView.findViewById(R.id.imageItemPoster);
            textItemName = itemView.findViewById(R.id.textItemName);
        }

        void bindItem(MovieResult movieResult, Context context) {
            if (!TextUtils.isEmpty(movieResult.getPosterPath())) {
                ImageAdapter.setPosterURL(imageItemPoster, movieResult.getPosterPath());
            } else {
                imageItemPoster.setImageResource(R.drawable.ic_android);
            }
            textItemName.setText(movieResult.getTitle());

            itemView.setOnClickListener(v -> {
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("type", "movie");
                i.putExtra("id", movieResult.getId());
                context.startActivity(i);
            });
        }
    }
}
