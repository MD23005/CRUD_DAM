package com.example.crudapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cliente {

    @PrimaryKey(autoGenerate = true)
    public int ID_Cliente;

    @ColumnInfo(name = "Nombre")
    public String nombre;

    @ColumnInfo(name = "DUI")
    public String dui;

    @ColumnInfo(name = "Telefono")
    public String telefono;

    @ColumnInfo(name = "Correo_Electronico")
    public String correoElectronico;

    public Cliente(String nombre,
                   String dui,
                   String telefono,
                   String correoElectronico) {

        this.nombre = nombre;
        this.dui = dui;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }
}