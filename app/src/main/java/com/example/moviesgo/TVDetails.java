package com.example.moviesgo;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class TVDetails {

    @SerializedName("name")
    String name;

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("backdrop_path")
    String backdropPath;

    @SerializedName("episode_run_time")
    int[] episodeRuntime;

    @SerializedName("first_air_date")
    String firstAirDate;

    @SerializedName("last_air_date")
    String lastAirdate;

    @SerializedName("number_of_episodes")
    String numberOfEpisodes;

    @SerializedName("number_of_seasons")
    String numberOfSeasons;

    @SerializedName("original_language")
    String originalLanguage;

    @SerializedName("overview")
    String overview;

    @SerializedName("popularity")
    String popularity;

    @SerializedName("status")
    String status;

    @SerializedName("tagline")
    String tagline;

    @SerializedName("vote_average")
    String voteAverage;

    @SerializedName("vote_count")
    String voteCount;

    @SerializedName("homepage")
    String homepage;

    public int[] getEpisodeRuntime() {
        return episodeRuntime;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getLastAirdate() {
        return lastAirdate;
    }

    public String getName() {
        return name;
    }

    public String getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public String getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getHomepage() {
        return homepage;
    }
}
