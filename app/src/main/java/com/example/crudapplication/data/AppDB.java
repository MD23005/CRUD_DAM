package com.example.crudapplication.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.crudapplication.dao.VehiculoDAO;
import com.example.crudapplication.entities.Vehiculo;

@Database(entities ={Vehiculo.class},version =1)
public abstract class AppDB
        extends RoomDatabase {
    private static AppDB instancia;
    public abstract VehiculoDAO vehiculoDAO();
    public static synchronized AppDB getInstance(Context context) {
        if (instancia == null) {
            instancia =
                    Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "db_alquileres")
                            .build();
        }
        return instancia;
    }
}
