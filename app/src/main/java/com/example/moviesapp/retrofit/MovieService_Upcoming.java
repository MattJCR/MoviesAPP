package com.example.moviesapp.retrofit;


import com.example.moviesapp.models.MovieFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService_Upcoming {
    @GET("movie/upcoming")
    Call<MovieFeed> getTopRated(@Query("api_key") String apiKey, @Query("language") String language);
}