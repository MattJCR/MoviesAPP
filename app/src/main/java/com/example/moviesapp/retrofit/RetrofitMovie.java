package com.example.moviesapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMovie {
    private static Retrofit retrofit;

    /* Indique su API Key de themoviedb.org */
    private static final String API_KEY = "bfb41ebc7a2213ee18b1e1684f912632";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    /*
    Si no exite la instancia retrofit la crea, indicando que se va a convertir un fichero JSON
    */
    public static Retrofit getRetrofitInstance(String id) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL + id)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static String getApiKey () {
        return API_KEY;
    }
}
