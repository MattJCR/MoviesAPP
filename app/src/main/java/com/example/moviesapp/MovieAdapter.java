package com.example.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.models.MovieDetail;
import com.example.moviesapp.models.MovieListed;
import com.example.moviesapp.activity_view_details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
    Defino un adaptador que hereda de RecyclerView.Adaptar y que definirá una clase aninada llamada moviesViewHolder
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.moviesViewHolder> {
    /*
    Atributos
    */
    public final Context context; //Almacena el contexto donde se ejecutará
    private ArrayList<MovieListed> list; //Almacenará las películas a mostrar
    private MovieAdapter.OnItemClickListener listener; //Listener para cuando se haga click

    //Defino un interface con el OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(MovieListed movie);
    }

    /*
    Constructor
    */
    public MovieAdapter(Context context) {
        this.list = new ArrayList<MovieListed>();
        this.context = context;
        setListener();
    }


    /*
    Asocio al atributo listener el onItemClickListener correspondiente y sobreescribo el método onItemClick pasando como
    argumento una película
    */
    private void setListener () {
        this.listener = new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieListed movie) {
                //Log.i("MYDEBUG", "onItemClick: ");
                Intent intent = new Intent(context, activity_view_details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MovieTitle", movie.getTitle());
                intent.putExtra("MovieVoteAverage",String.valueOf(movie.getVote_average()));
                intent.putExtra("MovieDesc", movie.getOverview());
                intent.putExtra("MovieRelease", movie.getRelease_date().replace("-","/"));
                float id = movie.getId();
                intent.putExtra("id", id);
                intent.putExtra("MovieImage",movie.getPoster_path());
                //Log.i("MYDEBUG", "onItemClick: MovieID " + String.valueOf(movie.getId()));

                context.startActivity(intent);
            }
        };
    }
    /*
    Sobreescribo onCreateViewHolder, donde  "inflo" la vista de cada item  y devuelve el viewholder que se crea pasándole la vista
    como parámetro
    */
    @Override
    public moviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_item, parent, false);
        moviesViewHolder tvh = new moviesViewHolder(itemView);
        return tvh;
    }
    /*
    Sobreescribe onViewHolder, haciendo que muestre la película que hay en el arraylist list en la posición que pasamos como
    parámetro
     */
    @Override
    public void onBindViewHolder(moviesViewHolder holder, int position) {
        final MovieListed movie = list.get(position);
        Log.i("MYDEBUG", "MovieAdapter_onBindViewHolder: " + movie.getTitle());
        holder.bindMovie(movie, listener);
    }

    /*
    Sobreescribe getItemCount devolviendo el número de películas que tenemos en el arraylist list.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /*
    Defino el viewHolder anidado que hereda de Recycler.ViewHolder necesario para que funcione el adaptador
     */
    public class moviesViewHolder extends RecyclerView.ViewHolder {
        /*
        Atributos
         */
        TextView tvName;
        //TextView tvRating;
        ImageView tvImage;
        RatingBar tvRatingBar;

        /*
            Constructor, enlazo los atributos con los elementos del layout
         */
        public moviesViewHolder(View itemView) {
            super(itemView);
            Log.i("MYDEBUG", "MovieAdapter_itemView.getId().moviesViewHolder: " + itemView.getId());
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvImage = (ImageView) itemView.findViewById(R.id.image_movie);
            tvRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBarMovie);
        }
        /*
        Defino un método que servirá para poner los datos de la película en los correspondientes textviews del layout e
        invocará al listener del adaptador cuando se haga click sobre la vista del viewHolder.
         */
        public void bindMovie(final MovieListed movie, final MovieAdapter.OnItemClickListener listener) {
            Log.i("MYDEBUG", "MovieAdapter_bindMovie: " + movie.getTitle());
            //Log.i("MYDEBUG", "tvName.getId(): " + tvName.getId());
            tvName.setText(movie.getTitle());
            //tvRating.setText(movie.getVote_average() + "/10");
            tvRatingBar.setRating(movie.getVote_average()/2);
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(tvImage);
            /*Coloco el Listener a la vista)*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }

    /*
    Método que limpia el array list de contenidos, carga los nuevos contenidos que se le pasan por parámetro e invoca a
    notifyDataSetChanged para hacer que se refresque la vista del RecyclerView
     */
    public void swap(List<MovieListed> newListMovies) {
        list.clear();
        list.addAll(newListMovies);
        notifyDataSetChanged();
    }
}