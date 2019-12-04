package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesapp.models.MovieFeed;
import com.example.moviesapp.retrofit.MovieService_TopRated;
import com.example.moviesapp.retrofit.RetrofitMovie;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_view_details extends AppCompatActivity {
    private TextView titulo,descripcion;
    private RatingBar rating;
    private ImageView layoutImage;
    MovieAdapter movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        titulo = (TextView) findViewById(R.id.textViewTitulo);
        descripcion = (TextView) findViewById(R.id.textView_Descripcion);
        layoutImage = findViewById(R.id.imageView_Activity);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        Intent intent = getIntent();
        descripcion.setText(intent.getStringExtra("MovieDesc"));
        titulo.setText(intent.getStringExtra("MovieTitle"));
        rating.setRating(Float.valueOf(intent.getStringExtra("MovieVoteAverage"))/2);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + intent.getStringExtra("MovieImage")).into(layoutImage);
    }

    public void loadSearch (String id) {
        /* Crea la instanncia de retrofit */
        MovieService_TopRated getMovie = RetrofitMovie.getRetrofitInstance(id).create(MovieService_TopRated.class);
        /* Se definen los parámetros de la llamada a la función getTopRated */
        Call<MovieFeed> call = getMovie.getTopRated(RetrofitMovie.getApiKey(), "es");
        /* Se hace una llamada asíncrona a la API */
        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    /* En caso de respuesta correcta */
                    case 200:
                        /* En el objeto data de la clase MovieFeed se almacena el JSON convertido a objetos*/
                        MovieFeed data = response.body();
                        /* Se actualizan los datos contenidos en el adaptador */
                        movieAdapter.swap(data.getResults());
                        /* Se notifica un cambio de datos para que se refresque la vista */
                        movieAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailure(Call<MovieFeed> call, Throwable t) {
                Toast.makeText(com.example.moviesapp.activity_view_details.this, "Error cargando películas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

