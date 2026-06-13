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
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Eliminar vehículo")
                                        .setMessage("¿Eliminar " + vehiculo.marca
                                                + " " + vehiculo.modelo + "?")
                                        .setPositiveButton("Eliminar", (dialog, which) -> {
                                            new Thread(() -> {
                                                db.vehiculoDAO().eliminar(vehiculo);
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