package com.example.crudapplication;

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

    public VehiculosFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        cargarVehiculos();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_vehiculos, container, false);

        db = AppDB.getInstance(requireContext());

        recyclerVehiculos = vista.findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fabAgregar = vista.findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v -> {
            AgregarVehiculoDialog dialogo = new AgregarVehiculoDialog(() -> cargarVehiculos());
            dialogo.show(getChildFragmentManager(), "AgregarVehiculoDialog");
        });

        return vista;
    }

    private void cargarVehiculos() {
        new Thread(() -> {
            lista = db.vehiculoDAO().obtenerTodos();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {

                    adapter = new VehiculoAdapter(
                            lista,
                            requireContext(),

                            // Detalle
                            (marca, modelo, año, placa, tipo_vehiculo, precio, estado, foto) -> {
                                Toast.makeText(getContext(),
                                        marca + " " + modelo,
                                        Toast.LENGTH_SHORT).show();
                            },

                            // Eliminar
                            (vehiculo, position) -> {

                                // 1. Primero consultamos a la base de datos en segundo plano si el vehículo está en uso
                                new Thread(() -> {
                                    // Contamos cuántos alquileres están usando el ID de este auto
                                    int alquileresActivos = db.vehiculoDAO().contarAlquileresDeVehiculo(vehiculo.getID_Auto());

                                    // 2. Regresamos al hilo de la interfaz gráfica para tomar una decisión
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(() -> {

                                            if (alquileresActivos > 0) {
                                                // BLOQUEO: El vehículo tiene llaves foráneas registradas
                                                new AlertDialog.Builder(requireContext())
                                                        .setTitle("No se puede eliminar")
                                                        .setMessage("Este vehículo está asignado a " + alquileresActivos + " registro(s) de alquiler. Debes eliminar primero esos registros para poder borrar el vehículo.")
                                                        .setPositiveButton("Aceptar", null)
                                                        .show();

                                            } else {
                                                // SEGURO: No hay alquileres que usen ese auto
                                                new AlertDialog.Builder(requireContext())
                                                        .setTitle("Eliminar vehículo")
                                                        .setMessage("¿Eliminar " + vehiculo.getMarca() + " " + vehiculo.getModelo() + "?") // (Ajustado con getters por consistencia)
                                                        .setPositiveButton("Eliminar", (dialog, which) -> {

                                                            new Thread(() -> {
                                                                db.vehiculoDAO().eliminar(vehiculo); // Usa tu método 'eliminar' existente

                                                                if (getActivity() != null) {
                                                                    getActivity().runOnUiThread(() -> {
                                                                        lista.remove(position);
                                                                        adapter.notifyItemRemoved(position);

                                                                        Toast.makeText(getContext(),
                                                                                "Vehículo eliminado",
                                                                                Toast.LENGTH_SHORT).show();
                                                                    });
                                                                }
                                                            }).start();
                                                        })
                                                        .setNegativeButton("Cancelar", null)
                                                        .show();
                                            }

                                        });
                                    }
                                }).start();
                            },

                           //Editar
                            (vehiculo) -> {
                                EditarVehiculoDialog dialogo = EditarVehiculoDialog.newInstance(
                                        vehiculo,
                                        () -> cargarVehiculos()
                                );
                                dialogo.show(getChildFragmentManager(), "EditarVehiculoDialog");
                            }
                    );

                    recyclerVehiculos.setAdapter(adapter);
                });
            }
        }).start();
    }
}