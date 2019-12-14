package com.example.moviesapp.ui.notifications;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.FavoritosSQLiteHelper;
import com.example.moviesapp.MovieAdapter_Favs;
import com.example.moviesapp.R;
import com.example.moviesapp.models.MovieFeed;
import com.example.moviesapp.models.MovieListed;
import com.example.moviesapp.retrofit.MovieService_TopRated;
import com.example.moviesapp.retrofit.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView_fav;
    MovieAdapter_Favs movieAdapter_fav;
    ArrayList<MovieListed> data;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        Log.i("MYDEBUG", "MovieFragment_TopRated_onCreateView: Entra");
        /* Enlaza el RecyclerView definido en el layout con el objeto recyclerView_top */
        recyclerView_fav = (RecyclerView) root.findViewById(R.id.recyclerView_Favorites);
        /* Establece que recyclerView_top tendrá un layout lineal, en concreto horizontal*/
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        /*  Indica que cada uno de los items va a tener un tamaño fijo*/
        recyclerView_fav.setHasFixedSize(true);
        /* Establece la  decoración por defecto de cada uno de lo items: una línea de división*/
        recyclerView_fav.addItemDecoration(new DividerItemDecoration(recyclerView_fav.getContext(), DividerItemDecoration.VERTICAL));
        /* Instancia un objeto de la clase MovieAdapter */
        movieAdapter_fav = new MovieAdapter_Favs(root.getContext());
        /* Establece el adaptador asociado a recyclerView_top */
        recyclerView_fav.setAdapter(movieAdapter_fav);
        /* Pone la animación por defecto de recyclerView_top */
        recyclerView_fav.setItemAnimator(new DefaultItemAnimator());
        loadFavorite();
        return root;
    }
    public void loadFavorite() {
        // writable porque vamos a insertar
        FavoritosSQLiteHelper usdbh = new FavoritosSQLiteHelper(getContext());
        SQLiteDatabase baseDeDatos = usdbh.getReadableDatabase();
        String[] columnasAConsultar = {"id"};
        Cursor cursor = baseDeDatos.rawQuery(" SELECT * FROM favoritas", null);
        data = new ArrayList<MovieListed>();
        while (cursor.moveToNext()){
            Log.i("MYDEBUG", "loadFavorite: " + cursor.getFloat(0));
            Log.i("MYDEBUG", "loadFavorite: " + cursor.getString(1));
            Log.i("MYDEBUG", "loadFavorite: " + cursor.getString(2));
            MovieListed movie = new MovieListed();
            movie.setId(cursor.getFloat(0));
            movie.setTitle(cursor.getString(1));
            movie.setPoster_path(cursor.getString(2));
            data.add(movie);
        }
        movieAdapter_fav.swap(data);
        /* Se notifica un cambio de datos para que se refresque la vista */
        movieAdapter_fav.notifyDataSetChanged();
    }
}