package com.example.moviesgo;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchActivity extends AppCompatActivity {

    private final List<MovieResult> movieResults = new ArrayList<>();
    private final List<TVResult> TVResults = new ArrayList<>();
    private String query;
    private RecyclerView rvSearch;
    private MovieSearchAdapter movieAdapter;
    private TVSearchAdapter tvAdapter;
    private ApiService apiService;
    private ProgressBar loadingSearch, loadingMoreSearch;
    private TextView textNoResults;
    private int currentPage = 1;
    private int totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        loadingSearch = findViewById(R.id.loadingSearch);
        loadingMoreSearch = findViewById(R.id.loadingMoreSearch);
        textNoResults = findViewById(R.id.textNoResults);
        TextView textSearchQuery = findViewById(R.id.textSearchQuery);
        rvSearch = findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new StaggeredGridLayoutManager(4, RecyclerView.VERTICAL));
        movieAdapter = new MovieSearchAdapter(movieResults, this);
        tvAdapter = new TVSearchAdapter(TVResults, this);
        loadingSearch.setVisibility(View.VISIBLE);

        apiService = ApiClient.getClient().create(ApiService.class);

        query = getIntent().getStringExtra("searchFor");
        textSearchQuery.setText(HtmlCompat.fromHtml("You searched for : <b>" + query + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY));
        String type = getIntent().getStringExtra("type");
        switch (type) {
            case "Movies" :
                searchForMovies();
                rvSearch.setAdapter(movieAdapter);
                break;
            case "TV Shows" :
                searchForTV();
                rvSearch.setAdapter(tvAdapter);
                break;
        }

        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvSearch.canScrollVertically(1)) {
                    if (currentPage <= totalPages) {
                        loadingMoreSearch.setVisibility(View.VISIBLE);
                        currentPage += 1;
                        if (type.equals("Movies"))
                            searchForMovies();
                        else searchForTV();
                    }
                }
            }
        });

        findViewById(R.id.imageBack).setOnClickListener(v -> onBackPressed());
    }

    private void searchForMovies() {
        Call<MovieRespon> call = apiService.searchMovie(MainActivity.MYAPI_KEY, MainActivity.LANGUAGE,
                query, currentPage);
        call.enqueue(new Callback<MovieRespon>() {
            @Override
            public void onResponse(@NonNull Call<MovieRespon> call,@NonNull Response<MovieRespon> response) {
                if (response.body() != null) {
                    if (response.body().getResults().size() > 0) {
                        loadingSearch.setVisibility(View.GONE);
                        loadingMoreSearch.setVisibility(View.GONE);
                        totalPages = response.body().getTotalPages();
                        int oldCount = movieResults.size();
                        movieResults.addAll(response.body().getResults());
                        movieAdapter.notifyItemRangeInserted(oldCount, movieResults.size());
                        checkSize(response.body().getResults().size());
                    } else {
                        loadingSearch.setVisibility(View.GONE);
                        loadingMoreSearch.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieRespon> call,@NonNull Throwable t) {
                loadingSearch.setVisibility(View.GONE);
                loadingMoreSearch.setVisibility(View.GONE);
                textNoResults.setVisibility(View.VISIBLE);
                textNoResults.setText(R.string.something_wrong);
            }
        });
    }

    private void searchForTV() {
        Call<TVRespon> call = apiService.searchTv(MainActivity.MYAPI_KEY, MainActivity.LANGUAGE,
                query, currentPage);
        call.enqueue(new Callback<TVRespon>() {
            @Override
            public void onResponse(@NonNull Call<TVRespon> call, @NonNull Response<TVRespon> response) {
                if (response.body() != null) {
                    if (response.body().getResults().size() > 0) {
                        loadingSearch.setVisibility(View.GONE);
                        loadingMoreSearch.setVisibility(View.GONE);
                        totalPages = response.body().getTotalPages();
                        int oldCount = TVResults.size();
                        TVResults.addAll(response.body().getResults());
                        tvAdapter.notifyItemRangeInserted(oldCount, TVResults.size());
                        checkSize(response.body().getResults().size());
                    } else {
                        loadingSearch.setVisibility(View.GONE);
                        loadingMoreSearch.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<TVRespon> call, @NonNull Throwable t) {
                loadingSearch.setVisibility(View.GONE);
                loadingMoreSearch.setVisibility(View.GONE);
                textNoResults.setVisibility(View.VISIBLE);
                textNoResults.setText(R.string.something_wrong);
            }
        });
    }

    private void checkSize(int resulst) {
        if (resulst == 0) {
            textNoResults.setVisibility(View.VISIBLE);
        } else {
            textNoResults.setVisibility(View.GONE);
        }
    }
}