package com.example.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.models.CreditsFeed;
import com.example.moviesapp.models.CreditsListed;
import com.example.moviesapp.models.MovieDetail;
import com.example.moviesapp.retrofit.CreditsService;
import com.example.moviesapp.retrofit.MovieDetailsService;
import com.example.moviesapp.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_view_details extends AppCompatActivity {
    private TextView titulo,descripcion,releaseDate,genre,studio;
    private RatingBar rating;
    private ImageView layoutImage;
    private Button trailer, fav;
    private String urlTrailer;
    CreditsAdapter creditsAdapter;
    int ID;
    String tittle_movie, picture;

    private RecyclerView recyclerView_Credits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        titulo = (TextView) findViewById(R.id.textViewTitulo);
        descripcion = (TextView) findViewById(R.id.textView_Descripcion);
        layoutImage = findViewById(R.id.imageView_Activity);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        releaseDate = (TextView) findViewById(R.id.textView_RealeaseDate);
        genre = (TextView) findViewById(R.id.textView_Genre);
        studio = (TextView) findViewById(R.id.textView_Studio);
        trailer = findViewById(R.id.button_trailer);
        fav = findViewById(R.id.button_corazon);
        urlTrailer = "";
        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (urlTrailer.length() > 0) {
                    i.setData(Uri.parse(urlTrailer));
                    startActivity(i);
                }else{
                    Toast.makeText(com.example.moviesapp.activity_view_details.this, "No existe el trailer.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.getCurrentTextColor() == Color.RED){
                    fav.setTextColor(Color.DKGRAY);
                    dropFavorite(ID);
                }else{
                    fav.setTextColor(Color.RED);
                    newFavorite(ID,tittle_movie,picture);
                }
            }
        });
        Intent intent = getIntent();
        /*descripcion.setText(intent.getStringExtra("MovieDesc"));
        titulo.setText(intent.getStringExtra("MovieTitle"));
        rating.setRating(Float.valueOf(intent.getStringExtra("MovieVoteAverage"))/2);
        releaseDate.setText(intent.getStringExtra("MovieRelease"));
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + intent.getStringExtra("MovieImage")).into(layoutImage);*/
        float id =  intent.getFloatExtra("id", 0);
        ID =( int) id;
        /* Enlaza el RecyclerView definido en el layout con el objeto recyclerView_top */
        recyclerView_Credits = (RecyclerView) findViewById(R.id.recycler_view_credits);
        /* Establece que recyclerView_top tendrá un layout lineal, en concreto horizontal*/
        recyclerView_Credits.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        /*  Indica que cada uno de los items va a tener un tamaño fijo*/
        recyclerView_Credits.setHasFixedSize(true);
        /* Establece la  decoración por defecto de cada uno de lo items: una línea de división*/
        recyclerView_Credits.addItemDecoration(new DividerItemDecoration(recyclerView_Credits.getContext(), DividerItemDecoration.VERTICAL));
        /* Instancia un objeto de la clase MovieAdapter */
        creditsAdapter = new CreditsAdapter(this);
        /* Establece el adaptador asociado a recyclerView_top */
        recyclerView_Credits.setAdapter(creditsAdapter);
        /* Pone la animación por defecto de recyclerView_top */
        recyclerView_Credits.setItemAnimator(new DefaultItemAnimator());


        //Log.i("MYDEBUG", "ANTES DE PINTAR LOS CREDITS: " + intent.getScheme());
        //Log.i("MYDEBUG", "ANTES DE PINTAR LOS CREDITS: " + intent.getStringExtra("MovieTitle"));
        //Log.i("MYDEBUG", "ANTES DE PINTAR LOS CREDITS: " + intent.getStringExtra("MovieID"));
        Log.i("MYDEBUG", "activity_view_details_VALOR IDPelicula: " + id);
        loadSearch_MovieDetails(String.valueOf(((int) id)));
        loadSearch_Credits(String.valueOf(((int) id)));
        fav.setTextColor(Color.WHITE);
        if (checkFavorite(ID)){
            fav.setTextColor(Color.RED);
        }else{
            fav.setTextColor(Color.WHITE);
        }
    }

    public void loadSearch_Credits(String id) {
        Log.i("MYDEBUG", "activity_view_details_loadSearch_ID PELICULA: " + id);
        /* Crea la instanncia de retrofit */
        CreditsService getCredit = RetrofitInstance.getRetrofitInstance().create(CreditsService.class);
        /* Se definen los parámetros de la llamada a la función getTopRated */
        Log.i("MYDEBUG", "activity_view_details_loadSearch_getCredits: " + getCredit.getCredits(id,RetrofitInstance.getApiKey(), "es").request());
        Call<CreditsFeed> call = getCredit.getCredits(id,RetrofitInstance.getApiKey(), "es");
        /* Se hace una llamada asíncrona a la API */
        call.enqueue(new Callback<CreditsFeed>() {
            @Override
            public void onResponse(Call<CreditsFeed> call, Response<CreditsFeed> response) {
                Log.i("MYDEBUG", "activity_view_details_loadSearch_onResponse code: " + response.code());
                switch (response.code()) {
                    /* En caso de respuesta correcta */
                    case 200:
                        /* En el objeto data de la clase MovieFeed se almacena el JSON convertido a objetos*/

                        CreditsFeed data = response.body();
                        for (CreditsListed a:data.getCast()) {
                            Log.i("MYDEBUG", "activity_view_details_loadSearch_onResponse_case200_Data: " + a.getName());
                        }
                        /* Se actualizan los datos contenidos en el adaptador */
                        creditsAdapter.swap(data.getCast());
                        /* Se notifica un cambio de datos para que se refresque la vista */
                        creditsAdapter.notifyDataSetChanged();
                        Log.i("MYDEBUG", "METE INFO CREDITS: ");
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailure(Call<CreditsFeed> call, Throwable t) {
                Toast.makeText(com.example.moviesapp.activity_view_details.this, "Error cargando los creditos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadSearch_MovieDetails(String id) {
        Log.i("MYDEBUG", "activity_view_details_loadSearch_ID PELICULA: " + id);
        /* Crea la instanncia de retrofit */
        MovieDetailsService getCredit = RetrofitInstance.getRetrofitInstance().create(MovieDetailsService.class);
        /* Se definen los parámetros de la llamada a la función getTopRated */
        Log.i("MYDEBUG", "activity_view_details_loadSearch_getCredits: " + getCredit.getMovieDetails(id,RetrofitInstance.getApiKey(), "es").request());
        Call<MovieDetail> call = getCredit.getMovieDetails(id,RetrofitInstance.getApiKey(), "es");
        /* Se hace una llamada asíncrona a la API */
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                Log.i("MYDEBUG", "activity_view_details_loadSearch_onResponse code: " + response.code());
                switch (response.code()) {
                    /* En caso de respuesta correcta */
                    case 200:
                        /* En el objeto data de la clase MovieFeed se almacena el JSON convertido a objetos*/

                        MovieDetail data = response.body();
                        descripcion.setText(data.getOverview());
                        titulo.setText(data.getTitle());
                        tittle_movie = data.getTitle();
                        picture = data.getPoster_path();
                        rating.setRating(Float.valueOf(data.getPopularity())/2);
                        releaseDate.setText(data.getRelease_date());
                        String generos = "", estudio = "";
                        for (Object a:data.getGenres()){
                            generos += a.toString().substring(a.toString().indexOf("name=") + 5,a.toString().length() - 1) + " ";
                            Log.i("MYDEBUG", "onResponse: " + a.toString().substring(a.toString().indexOf("name=") + 5,a.toString().length() - 1));
                        }
                        genre.setText(generos);
                        for (Object a:data.getStudio()){
                            estudio += a.toString().substring(a.toString().indexOf("name=") + 5,a.toString().indexOf("origin_country=") - 2) + " ";
                            Log.i("MYDEBUG", "onResponse: " + a.toString().substring(a.toString().indexOf("name=") + 5,a.toString().indexOf("origin_country=") - 2));
                        }
                        studio.setText(estudio);
                        urlTrailer = data.getHomepage();
                        Log.i("MYDEBUG", "URL TRAILER: ." + urlTrailer + ".");
                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + data.getPoster_path()).into(layoutImage);
                        Log.i("MYDEBUG", "METE INFO CREDITS: ");
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Toast.makeText(com.example.moviesapp.activity_view_details.this, "Error cargando los creditos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public long newFavorite(int id, String nombre, String url) {
        // writable porque vamos a insertar
        FavoritosSQLiteHelper usdbh = new FavoritosSQLiteHelper(this);
        SQLiteDatabase baseDeDatos = usdbh.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("id", id);
        valoresParaInsertar.put("nombre", nombre);
        valoresParaInsertar.put("imagen", url);
        return baseDeDatos.insert("favoritas", null, valoresParaInsertar);
    }
    public long dropFavorite(int id) {
        // writable porque vamos a insertar
        FavoritosSQLiteHelper usdbh = new FavoritosSQLiteHelper(this);
        SQLiteDatabase baseDeDatos = usdbh.getWritableDatabase();
        String[] argumentos = {String.valueOf(id)};
        return baseDeDatos.delete("favoritas", "id = ?", argumentos);
    }
    public boolean checkFavorite(int id) {
        // writable porque vamos a insertar
        FavoritosSQLiteHelper usdbh = new FavoritosSQLiteHelper(this);
        SQLiteDatabase baseDeDatos = usdbh.getReadableDatabase();
        String[] columnasAConsultar = {"id"};
        Cursor cursor = baseDeDatos.rawQuery(" SELECT id FROM favoritas WHERE id='" + id + "' ", null);
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }
}

