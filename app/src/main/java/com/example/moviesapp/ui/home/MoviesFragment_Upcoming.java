package com.example.moviesapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.MovieAdapter;
import com.example.moviesapp.R;
import com.example.moviesapp.models.MovieFeed;
import com.example.moviesapp.retrofit.MovieService_TopRated;
import com.example.moviesapp.retrofit.MovieService_Upcoming;
import com.example.moviesapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//TODO Borrar esta clase.

public class MoviesFragment_Upcoming extends Fragment {
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies, container, false);
        /* Enlaza el RecyclerView definido en el layout con el objeto recyclerView_top */
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_MoviesUpcoming);
        /* Establece que recyclerView_top tendrá un layout lineal, en concreto horizontal*/
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        /*  Indica que cada uno de los items va a tener un tamaño fijo*/
        recyclerView.setHasFixedSize(true);
        /* Establece la  decoración por defecto de cada uno de lo items: una línea de división*/
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        /* Instancia un objeto de la clase MovieAdapter */
        movieAdapter = new MovieAdapter(root.getContext());
        /* Establece el adaptador asociado a recyclerView_top */
        recyclerView.setAdapter(movieAdapter);
        /* Pone la animación por defecto de recyclerView_top */
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadSearch_Upcoming();
        return root;
    }
    public void loadSearch_TopRated() {
        /* Crea la instanncia de retrofit */
        MovieService_TopRated getMovie = RetrofitInstance.getRetrofitInstance().create(MovieService_TopRated.class);
        /* Se definen los parámetros de la llamada a la función getTopRated */
        Call<MovieFeed> call = getMovie.getTopRated(RetrofitInstance.getApiKey(), "es");
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
                //Toast.makeText(MainActivity.this, "Error cargando películas", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadSearch_Upcoming() {
        /* Crea la instanncia de retrofit */
        MovieService_Upcoming getMovie = RetrofitInstance.getRetrofitInstance().create(MovieService_Upcoming.class);
        /* Se definen los parámetros de la llamada a la función getTopRated */
        Call<MovieFeed> call = getMovie.getTopRated(RetrofitInstance.getApiKey(), "es");
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
                //Toast.makeText(MainActivity.this, "Error cargando películas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}