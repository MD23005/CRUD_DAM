package com.example.crudapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Ejemplo por si usas TextViews

import com.example.crudapplication.R;

public class HomeFragment extends Fragment {

    // 1. El constructor público vacío es obligatorio para los fragmentos
    public HomeFragment() {
        // Requiere constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvAutos = vista.findViewById(R.id.tv_autos_count);
        tvAutos.setText("12"); // Ejemplo de uso

        return vista;
    }
}