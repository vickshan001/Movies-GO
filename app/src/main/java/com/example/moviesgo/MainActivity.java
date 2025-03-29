package com.example.moviesgo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView rvMoviePopular, rvMovieNowPlaying, rvMovieTopRated, rvMovieUpcoming;
    private ProgressBar loadingMoviePopular, loadingMovieNowPlaying, loadingMovieTopRated, loadingMovieUpcoming;
    private ProgressBar loadingMainMoviePopular, loadingMainMovieNowPlaying, loadingMainMovieTopRated, loadingMainMovieUpcoming;
    private MovieAdapter moviePopularAdapter, movieNowPlayingAdapter, movieUpcomingAdapter, movieTopRatedAdapter;
    private final List<MovieResult> moviePopularResults = new ArrayList<>();
    private final List<MovieResult> movieNowPlayingResults = new ArrayList<>();
    private final List<MovieResult> movieUpcomingResults = new ArrayList<>();
    private final List<MovieResult> movieTopRatedResults = new ArrayList<>();
    private int currentPageMoviePopular = 1, currentPageMovieNowPlaying = 1,
            currentPageMovieUpcoming = 1, currentPageMovieTopRated = 1;
    private int totalPagesMoviePopular = 1, totalPagesMovieNowPlaying = 1, totalPagesMovieUpcoming =1,
            totalPagesMovieTopRated = 1;

    private RecyclerView rvTvPopular, rvTvTopRated, rvTvOnAir;
    private ProgressBar loadingTvPopular, loadingTvTopRated, loadingTvOnAir;
    private ProgressBar loadingMainTvPopular, loadingMainTvTopRated, loadingMainTvOnAir;
    private TVAdapter tvPopularAdapter, tvTopRatedAdapter, tvOnAirAdapter;
    private final List<TVResult> tvPopularResults = new ArrayList<>();
    private final List<TVResult> tvTopRatedResults = new ArrayList<>();
    private final List<TVResult> tvOnAirResults = new ArrayList<>();
    private int currentPageTVPopular = 1, currentPageTVTopRated = 1, currentPageTVOnAir = 1;
    private int totalPagesTVPopular = 1, totalPagesTVTopRated = 1, totalPagesTVOnAir = 1;

    //TODO Silahkan diganti dengan API Key TMDB Anda
    public static final String MYAPI_KEY = "ac9e96ff2eb00ebee311eef9ddcdffe2";

    public static final String LANGUAGE = "en-GB";

    private String searchType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        setPopularMovies();
        setNowPlayingMovies();
        setUpcomingMovies();
        setTopRatedMovies();

        setPopularTV();
        setTopRatedTV();
        setOnAirTV();

        findViewById(R.id.imageSearch).setOnClickListener(v -> dialogSearch());

    }

    private void setPopularMovies() {
        rvMoviePopular = findViewById(R.id.rvMoviePopular);
        moviePopularAdapter = new MovieAdapter(moviePopularResults, this);
        loadingMoviePopular = findViewById(R.id.loadingMoviePopular);
        loadingMainMoviePopular = findViewById(R.id.loadingMainMoviePopular);
        loadingMainMoviePopular.setVisibility(View.VISIBLE);
        getPopularMovies();
        rvMoviePopular.setAdapter(moviePopularAdapter);

        rvMoviePopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMoviePopular.canScrollHorizontally(1)) {
                    loadingMoviePopular.setVisibility(View.VISIBLE);
                    if (currentPageMoviePopular <= totalPagesMoviePopular) {
                        currentPageMoviePopular += 1;
                        getPopularMovies();
                    }
                }
            }
        });
    }

    private void getPopularMovies() {
        Call<MovieRespon> call = apiService.getPopularMovies(MYAPI_KEY, LANGUAGE, currentPageMoviePopular);
        call.enqueue(new Callback<MovieRespon>() {
            @Override
            public void onResponse(@NonNull Call<MovieRespon> call,@NonNull Response<MovieRespon> response) {
                if (response.body() != null) {
                    totalPagesMoviePopular = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMoviePopular.setVisibility(View.GONE);
                        loadingMoviePopular.setVisibility(View.GONE);
                        int oldCount = moviePopularResults.size();
                        moviePopularResults.addAll(response.body().getResults());
                        moviePopularAdapter.notifyItemChanged(oldCount, moviePopularResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieRespon> call,@NonNull Throwable t) {
                loadingMainMoviePopular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading popular movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNowPlayingMovies() {
        rvMovieNowPlaying = findViewById(R.id.rvMovieNowPlaying);
        movieNowPlayingAdapter = new MovieAdapter(movieNowPlayingResults, this);
        loadingMovieNowPlaying = findViewById(R.id.loadingMovieNowPlaying);
        loadingMainMovieNowPlaying = findViewById(R.id.loadingMainMovieNowPlaying);
        loadingMainMovieNowPlaying.setVisibility(View.VISIBLE);
        getNowPlayingMovies();
        rvMovieNowPlaying.setAdapter(movieNowPlayingAdapter);

        rvMovieNowPlaying.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMovieNowPlaying.canScrollHorizontally(1)) {
                    loadingMovieNowPlaying.setVisibility(View.VISIBLE);
                    if (currentPageMovieNowPlaying <= totalPagesMovieNowPlaying) {
                        currentPageMovieNowPlaying += 1;
                        getNowPlayingMovies();
                    }
                }
            }
        });
    }

    private void getNowPlayingMovies() {
        Call<MovieRespon> call = apiService.getNowPlayingMovies(MYAPI_KEY, LANGUAGE, currentPageMovieNowPlaying);
        call.enqueue(new Callback<MovieRespon>() {
            @Override
            public void onResponse(@NonNull Call<MovieRespon> call,@NonNull Response<MovieRespon> response) {
                if (response.body() != null) {
                    totalPagesMovieNowPlaying = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMovieNowPlaying.setVisibility(View.GONE);
                        loadingMovieNowPlaying.setVisibility(View.GONE);
                        int oldCount = movieNowPlayingResults.size();
                        movieNowPlayingResults.addAll(response.body().getResults());
                        movieNowPlayingAdapter.notifyItemChanged(oldCount, movieNowPlayingResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieRespon> call,@NonNull Throwable t) {
                loadingMainMovieNowPlaying.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "\n" +
                                "An error occurred loading now playing movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpcomingMovies() {
        rvMovieUpcoming = findViewById(R.id.rvUpcomingMovie);
        movieUpcomingAdapter = new MovieAdapter(movieUpcomingResults, this);
        loadingMovieUpcoming = findViewById(R.id.loadingMovieUpcoming);
        loadingMainMovieUpcoming = findViewById(R.id.loadingMainMovieUpcoming);
        loadingMainMovieUpcoming.setVisibility(View.VISIBLE);
        getUpcomingMovies();
        rvMovieUpcoming.setAdapter(movieUpcomingAdapter);

        rvMovieUpcoming.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMovieUpcoming.canScrollHorizontally(1)) {
                    loadingMovieUpcoming.setVisibility(View.VISIBLE);
                    if (currentPageMovieUpcoming <= totalPagesMovieUpcoming) {
                        currentPageMovieUpcoming += 1;
                        getUpcomingMovies();
                    }
                }
            }
        });
    }

    private void getUpcomingMovies() {
        Call<MovieRespon> call = apiService.getUpcomingMovies(MYAPI_KEY, LANGUAGE, currentPageMovieUpcoming);
        call.enqueue(new Callback<MovieRespon>() {
            @Override
            public void onResponse(@NonNull Call<MovieRespon> call,@NonNull Response<MovieRespon> response) {
                if (response.body() != null) {
                    totalPagesMovieUpcoming = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMovieUpcoming.setVisibility(View.GONE);
                        loadingMovieUpcoming.setVisibility(View.GONE);
                        int oldCount = movieUpcomingResults.size();
                        movieUpcomingResults.addAll(response.body().getResults());
                        movieUpcomingAdapter.notifyItemChanged(oldCount, movieUpcomingResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieRespon> call,@NonNull Throwable t) {
                loadingMainMovieUpcoming.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "\n" +
                                "An error occurred loading upcoming movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTopRatedMovies() {
        rvMovieTopRated = findViewById(R.id.rvMovieTopRated);
        movieTopRatedAdapter = new MovieAdapter(movieTopRatedResults, this);
        loadingMovieTopRated = findViewById(R.id.loadingMovieTopRated);
        loadingMainMovieTopRated = findViewById(R.id.loadingMainMovieTopRated);
        loadingMainMovieTopRated.setVisibility(View.VISIBLE);
        getTopRatedMovies();
        rvMovieTopRated.setAdapter(movieTopRatedAdapter);

        rvMovieTopRated.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMovieTopRated.canScrollHorizontally(1)) {
                    loadingMovieTopRated.setVisibility(View.VISIBLE);
                    if (currentPageMovieTopRated <= totalPagesMovieTopRated) {
                        currentPageMovieTopRated += 1;
                        getTopRatedMovies();
                    }
                }
            }
        });
    }

    private void getTopRatedMovies() {
        Call<MovieRespon> call = apiService.getTopRatedMovies(MYAPI_KEY, LANGUAGE, currentPageMovieTopRated);
        call.enqueue(new Callback<MovieRespon>() {
            @Override
            public void onResponse(@NonNull Call<MovieRespon> call,@NonNull Response<MovieRespon> response) {
                if (response.body() != null) {
                    totalPagesMovieTopRated = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMovieTopRated.setVisibility(View.GONE);
                        loadingMovieTopRated.setVisibility(View.GONE);
                        int oldCount = movieTopRatedResults.size();
                        movieTopRatedResults.addAll(response.body().getResults());
                        movieTopRatedAdapter.notifyItemChanged(oldCount, movieTopRatedResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieRespon> call,@NonNull Throwable t) {
                loadingMainMovieTopRated.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading top rated movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPopularTV() {
        rvTvPopular = findViewById(R.id.rvTvPopular);
        tvPopularAdapter = new TVAdapter(tvPopularResults, this);
        loadingTvPopular = findViewById(R.id.loadingTvPopular);
        loadingMainTvPopular = findViewById(R.id.loadingMainTvPopular);
        loadingMainTvPopular.setVisibility(View.VISIBLE);
        getPopularTV();
        rvTvPopular.setAdapter(tvPopularAdapter);

        rvTvPopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvTvPopular.canScrollHorizontally(1)) {
                    loadingTvPopular.setVisibility(View.VISIBLE);
                    if (currentPageTVPopular <= totalPagesTVPopular) {
                        currentPageTVPopular += 1;
                        getPopularTV();
                    }
                }
            }
        });
    }

    private void getPopularTV() {
        Call<TVRespon> call = apiService.getTvPopular(MYAPI_KEY, LANGUAGE, currentPageTVPopular);
        call.enqueue(new Callback<TVRespon>() {
            @Override
            public void onResponse(@NonNull Call<TVRespon> call,@NonNull Response<TVRespon> response) {
                if (response.body() != null) {
                    totalPagesTVPopular = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainTvPopular.setVisibility(View.GONE);
                        loadingTvPopular.setVisibility(View.GONE);
                        int oldCount = tvPopularResults.size();
                        tvPopularResults.addAll(response.body().getResults());
                        tvPopularAdapter.notifyItemChanged(oldCount, tvPopularResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVRespon> call,@NonNull Throwable t) {
                loadingMainTvPopular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading popular tv shows",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTopRatedTV() {
        rvTvTopRated = findViewById(R.id.rvTvTopRated);
        tvTopRatedAdapter = new TVAdapter(tvTopRatedResults, this);
        loadingTvTopRated = findViewById(R.id.loadingTvTopRated);
        loadingMainTvTopRated = findViewById(R.id.loadingMainTvTopRated);
        loadingMainTvTopRated.setVisibility(View.VISIBLE);
        getTopRatedTV();
        rvTvTopRated.setAdapter(tvTopRatedAdapter);

        rvTvTopRated.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvTvTopRated.canScrollHorizontally(1)) {
                    loadingTvTopRated.setVisibility(View.VISIBLE);
                    if (currentPageTVTopRated <= totalPagesTVTopRated) {
                        currentPageTVTopRated += 1;
                        getTopRatedTV();
                    }
                }
            }
        });
    }

    private void getTopRatedTV() {
        Call<TVRespon> call = apiService.getTvTopRated(MYAPI_KEY, LANGUAGE, currentPageTVTopRated);
        call.enqueue(new Callback<TVRespon>() {
            @Override
            public void onResponse(@NonNull Call<TVRespon> call,@NonNull Response<TVRespon> response) {
                if (response.body() != null) {
                    totalPagesTVTopRated = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingTvTopRated.setVisibility(View.GONE);
                        loadingMainTvTopRated.setVisibility(View.GONE);
                        int oldCount = tvTopRatedResults.size();
                        tvTopRatedResults.addAll(response.body().getResults());
                        tvTopRatedAdapter.notifyItemChanged(oldCount, tvTopRatedResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVRespon> call,@NonNull Throwable t) {
                loadingMainTvTopRated.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading top rated tv shows",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnAirTV() {
        rvTvOnAir = findViewById(R.id.rvTvOnAir);
        tvOnAirAdapter = new TVAdapter(tvOnAirResults, this);
        loadingTvOnAir = findViewById(R.id.loadingTvOnAir);
        loadingMainTvOnAir = findViewById(R.id.loadingMainTvOnAir);
        loadingMainTvOnAir.setVisibility(View.VISIBLE);
        getOnAirTV();
        rvTvOnAir.setAdapter(tvOnAirAdapter);

        rvTvOnAir.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvTvOnAir.canScrollHorizontally(1)) {
                    loadingTvOnAir.setVisibility(View.VISIBLE);
                    if (currentPageTVOnAir <= totalPagesTVOnAir) {
                        currentPageTVOnAir += 1;
                        getOnAirTV();
                    }
                }
            }
        });
    }

    private void getOnAirTV() {
        Call<TVRespon> call = apiService.getTvOnAir(MYAPI_KEY, LANGUAGE, currentPageTVOnAir);
        call.enqueue(new Callback<TVRespon>() {
            @Override
            public void onResponse(@NonNull Call<TVRespon> call,@NonNull Response<TVRespon> response) {
                if (response.body() != null) {
                    totalPagesTVOnAir = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainTvOnAir.setVisibility(View.GONE);
                        loadingTvOnAir.setVisibility(View.GONE);
                        int oldCount = tvOnAirResults.size();
                        tvOnAirResults.addAll(response.body().getResults());
                        tvOnAirAdapter.notifyItemChanged(oldCount, tvOnAirResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVRespon> call,@NonNull Throwable t) {
                loadingMainTvOnAir.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading on air tv shows",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialogSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_search, null);

        EditText inputSearch = v.findViewById(R.id.inputSearch);
        ImageView imageDoSearch = v.findViewById(R.id.imageDoSearch);
        RadioGroup radioGroup = v.findViewById(R.id.radioGroup);
        RadioButton radioButtonMovie = v.findViewById(R.id.radioButtonMovie);
        RadioButton radioButtonTv = v.findViewById(R.id.radioButtonTv);

        builder.setView(v);
        AlertDialog dialogSearch = builder.create();
        if (dialogSearch.getWindow() != null)
            dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonMovie) {
                searchType = radioButtonMovie.getText().toString();
            } else {
                searchType = radioButtonTv.getText().toString();
            }
        });
        imageDoSearch.setOnClickListener(view -> doSearch(inputSearch.getText().toString()));

        inputSearch.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                doSearch(inputSearch.getText().toString());
            }
            return false;
        });

        dialogSearch.show();
    }

    private void doSearch(String query) {
        if (query.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Query not Valid",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchType == null) {
            Toast.makeText(getApplicationContext(), "Please choose movie or tv show",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        i.putExtra("type", searchType);
        i.putExtra("searchFor", query);
        startActivity(i);
    }
}