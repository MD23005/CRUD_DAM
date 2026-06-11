package com.example.crudapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Vehiculo {
    @PrimaryKey(autoGenerate = true)
    public int ID_Auto;

    @ColumnInfo (name ="marca")
    public String marca;
    @ColumnInfo (name ="modelo")
    public String modelo;
    @ColumnInfo (name ="año")
    public String año;
    @ColumnInfo (name ="placa")
    public String placa;
    @ColumnInfo (name ="tipo_vehiculo")
    public String tipo_vehiculo;
    @ColumnInfo (name ="precio")
    public String precio;
    @ColumnInfo (name = "Estado")
    public String estado;
    @ColumnInfo(name ="Foto")
    public String foto;

    public Vehiculo(String marca, String modelo, String año, String placa,
                    String tipo_vehiculo, String precio,String estado, String foto)
    {
        this.marca=marca;
        this.modelo=modelo;
        this.año =año;
        this.placa =placa;
        this.tipo_vehiculo =tipo_vehiculo;
        this.precio=precio;
        this.estado=estado;
        this.foto=foto;

    }
}
