package com.example.moviesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritosSQLiteHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "movie_app", NOMBRE_TABLA_FAVORITAS = "favoritas";
    private static final int VERSION_BASE_DE_DATOS = 1;
    public FavoritosSQLiteHelper(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id integer primary key, nombre text, imagen text)", NOMBRE_TABLA_FAVORITAS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}