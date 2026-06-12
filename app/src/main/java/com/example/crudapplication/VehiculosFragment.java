package com.example.crudapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.adapters.VehiculoAdapter;
import com.example.crudapplication.data.AppDB;
import com.example.crudapplication.entities.Vehiculo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VehiculosFragment extends Fragment {

    RecyclerView recyclerVehiculos;
    VehiculoAdapter adapter;
    AppDB db;
    List<Vehiculo> lista;

    // Constructor público vacío obligatorio
    public VehiculosFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sigue cargando los vehículos cada vez que el fragmento vuelve a ser visible
        cargarVehiculos();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Inflamos el diseño que modificamos anteriormente
        View vista = inflater.inflate(R.layout.fragment_vehiculos, container, false);

        // 2. Inicializamos la base de datos usando el contexto del fragmento
        db = AppDB.getInstance(requireContext());

        // 3. Vinculamos y configuramos el RecyclerView usando "vista.findViewById"
        recyclerVehiculos = vista.findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(getContext()));

        // 4. Configuramos el FAB para abrir la actividad de agregar
        FloatingActionButton fabAgregar = vista.findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v -> {
            // 1. Instanciamos el diálogo y pasamos la acción para refrescar la lista al guardar
            AgregarVehiculoDialog dialogo = new AgregarVehiculoDialog(() -> {
                cargarVehiculos(); // Esto vuelve a leer Room y actualiza el RecyclerView
            });

            // 2. Mostramos el diálogo en pantalla
            // Usamos getChildFragmentManager() porque estamos dentro de otro Fragmento
            dialogo.show(getChildFragmentManager(), "AgregarVehiculoDialog");
        });

        return vista;
    }

    private void cargarVehiculos() {
        new Thread(() -> {
            lista = db.vehiculoDAO().obtenerTodos();

            // Los fragmentos usan getActivity().runOnUiThread para tocar la interfaz desde otro hilo
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // Pasamos requireContext() en lugar de "this" para el adaptador
                    adapter = new VehiculoAdapter(lista, requireContext(),
                            // Click normal
                            (marca, modelo, año, placa, tipo_vehiculo, precio, estado, foto) -> {
                                Toast.makeText(getContext(), marca + " " + modelo, Toast.LENGTH_SHORT).show();
                            },
                            // Click eliminar
                            (vehiculo, position) -> {
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Eliminar vehículo")
                                        .setMessage("¿Eliminar " + vehiculo.marca + " " + vehiculo.modelo + "?")
                                        .setPositiveButton("Eliminar", (dialog, which) -> {
                                            new Thread(() -> {
                                                db.vehiculoDAO().eliminar(vehiculo);
                                                if (getActivity() != null) {
                                                    getActivity().runOnUiThread(() -> {
                                                        lista.remove(position);
                                                        adapter.notifyItemRemoved(position);
                                                        Toast.makeText(getContext(), "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                                                    });
                                                }
                                            }).start();
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .show();
                            }
                    );
                    recyclerVehiculos.setAdapter(adapter);
                });
            }
        }).start();
    }
}