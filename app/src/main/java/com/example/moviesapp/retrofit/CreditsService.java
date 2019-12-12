package com.example.moviesapp.retrofit;


import com.example.moviesapp.models.CreditsFeed;
import com.example.moviesapp.models.MovieFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CreditsService {
    @GET("/3/movie/{id}/credits")
    Call<CreditsFeed> getCredits(@Path("id") String id, @Query("api_key") String apiKey, @Query("language") String language);
}