package com.example.crudapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.adapters.AlquilerAdapter;
import com.example.crudapplication.data.AppDB;
import com.example.crudapplication.entities.AlquilarVehiculo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.crudapplication.dao.AlquilerDAO;

import java.util.List;

public class AlquilarFragment extends Fragment {

    private RecyclerView recyclerAlquileres;
    private FloatingActionButton fabAgregarAlquiler;
    private AppDB db;
    private AlquilerAdapter adapter;
    private List<AlquilarVehiculo> lista;

    // Aquí irá tu adaptador una vez lo crees, ej: AlquilerAdapter adapter;

    public AlquilarFragment() {
        // Constructor público vacío obligatorio
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarga los registros de la base de datos de manera asíncrona cada vez que se visualiza la sección
        cargarAlquileres();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_alquilar, container, false);

        db = AppDB.getInstance(requireContext());

        recyclerAlquileres = vista.findViewById(R.id.recyclerAlquileres);
        fabAgregarAlquiler = vista.findViewById(R.id.fabAgregarAlquiler);

        // Vinculamos la orientación vertical para las tarjetas de item_alquiler
        recyclerAlquileres.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAgregarAlquiler.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Próximamente: Diálogo nuevo alquiler", Toast.LENGTH_SHORT).show();
            // Aquí mandaremos a llamar al DialogFragment de inserción
        });

        return vista;
    }

    private void cargarAlquileres() {
        new Thread(() -> {
            // Consultamos todos los alquileres almacenados en la base de datos
            lista = db.alquilerDAO().obtenerTodos(); // Asegúrate de que tu DAO tenga este método

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // Instanciamos el adaptador pasándole la lista y el contexto obligatorio
                    adapter = new AlquilerAdapter(lista, requireContext());

                    // Asignamos el adaptador al RecyclerView de la interfaz
                    recyclerAlquileres.setAdapter(adapter);
                });
            }
        }).start();
    }
}