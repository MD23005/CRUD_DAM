package com.example.crudapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.crudapplication.entities.Vehiculo;

import java.util.List;

@Dao
public interface VehiculoDAO {
    @Insert
    void crear (Vehiculo vehiculo);
    @Update
    void actualizar (Vehiculo vehiculo);
    @Delete
    void eliminar (Vehiculo vehiculo);

    @Query("SELECT * FROM Vehiculo WHERE Estado != 'Eliminado' ORDER BY ID_Auto DESC")
    List<Vehiculo> obtenerTodos();

    @Query("SELECT * FROM Vehiculo WHERE ID_Auto = :id")
    Vehiculo obtenerPorId(int id);

    @Query("UPDATE Vehiculo SET Estado = :estado WHERE ID_Auto = :id")
    int cambiarEstado(int id, String estado);

    @Query("SELECT * FROM Vehiculo WHERE Placa = :placa LIMIT 1")
    Vehiculo buscarPorPlaca(String placa);
}
