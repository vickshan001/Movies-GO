package com.example.moviesgo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TVRespon {

    @SerializedName("page")
    int page;

    @SerializedName("results")
    private final List<TVResult> results = new ArrayList<>();

    @SerializedName("total_results")
    int totalResults;

    @SerializedName("total_pages")
    int totalPages;

    public int getPage() {
        return page;
    }

    public List<TVResult> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
