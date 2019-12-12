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

import com.example.moviesapp.models.CreditsListed;
import com.example.moviesapp.models.MovieListed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
    Defino un adaptador que hereda de RecyclerView.Adaptar y que definirá una clase aninada llamada moviesViewHolder
 */
public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.creditViewHolder> {
    /*
    Atributos
    */
    public final Context context; //Almacena el contexto donde se ejecutará
    private ArrayList<CreditsListed> list; //Almacenará las películas a mostrar
    private CreditsAdapter.OnItemClickListener listener; //Listener para cuando se haga click

    //Defino un interface con el OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(CreditsListed credits);
    }

    /*
    Constructor
    */
    public CreditsAdapter(Context context) {
        this.list = new ArrayList<CreditsListed>();
        this.context = context;
        setListener();
    }


    /*
    Asocio al atributo listener el onItemClickListener correspondiente y sobreescribo el método onItemClick pasando como
    argumento una película
    */
    private void setListener () {
        this.listener = new CreditsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CreditsListed credit) {
                Log.i("MYDEBUG", "onItemClick: ");
                Intent intent = new Intent(context, activity_view_details.class);
                intent.putExtra("CreditName", credit.getName());
                intent.putExtra("CreditPathImage",credit.getProfile_path());
                context.startActivity(intent);
            }
        };
    }
    /*
    Sobreescribo onCreateViewHolder, donde  "inflo" la vista de cada item  y devuelve el viewholder que se crea pasándole la vista
    como parámetro
    */
    @Override
    public creditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_credit_item, parent, false);
        creditViewHolder tvh = new creditViewHolder(itemView);
        Log.i("MYDEBUG", "onCreateViewHolder: ");
        return tvh;
    }
    /*
    Sobreescribe onViewHolder, haciendo que muestre la película que hay en el arraylist list en la posición que pasamos como
    parámetro
     */
    @Override
    public void onBindViewHolder(creditViewHolder holder, int position) {
        final CreditsListed credit = list.get(position);
        Log.i("MYDEBUG", "onBindViewHolder: " + credit.getName());
        holder.bindMovie(credit, listener);
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
    public class creditViewHolder extends RecyclerView.ViewHolder {
        /*
        Atributos
         */
        TextView tvCredit;
        ImageView tvImageCredit;

        /*
            Constructor, enlazo los atributos con los elementos del layout
         */
        public creditViewHolder(View itemView) {
            super(itemView);
            Log.i("MYDEBUG", "itemView.getId().moviesViewHolder: " + itemView.getId());
            tvCredit = (TextView) itemView.findViewById(R.id.textView_Credit);
            tvImageCredit = (ImageView) itemView.findViewById(R.id.imageView_Credit);
        }
        /*
        Defino un método que servirá para poner los datos de la película en los correspondientes textviews del layout e
        invocará al listener del adaptador cuando se haga click sobre la vista del viewHolder.
         */
        public void bindMovie(final CreditsListed credit, final CreditsAdapter.OnItemClickListener listener) {
            Log.i("MYDEBUG", "bindMovie: " + credit.getName());
            //Log.i("MYDEBUG", "tvName.getId(): " + tvName.getId());
            tvCredit.setText(credit.getName());
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + credit.getProfile_path()).into(tvImageCredit);
            /*Coloco el Listener a la vista)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(credit);
                }
            });*/
        }
    }

    /*
    Método que limpia el array list de contenidos, carga los nuevos contenidos que se le pasan por parámetro e invoca a
    notifyDataSetChanged para hacer que se refresque la vista del RecyclerView
     */
    public void swap(List<CreditsListed> newListCredit) {
        list.clear();
        list.addAll(newListCredit);
        notifyDataSetChanged();
    }
}