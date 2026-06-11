package com.example.crudapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.adapters.VehiculoAdapter;
import com.example.crudapplication.data.AppDB;
import com.example.crudapplication.entities.Vehiculo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VehiculosActivity extends AppCompatActivity {

    RecyclerView recyclerVehiculos;
    VehiculoAdapter adapter;
    AppDB db;
    List<Vehiculo> lista; // ← variable de clase

    @Override
    protected void onResume() {
        super.onResume();
        cargarVehiculos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        db = AppDB.getInstance(this);

        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        // FAB aquí en el hilo principal
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, AgregarVehiculoActivity.class))
        );

        // Menú inferior
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_vehiculos);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_vehiculos) {
                return true;
            } else if (id == R.id.nav_clientes) {
                startActivity(new Intent(this, ClientesActivity.class));
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
            }
            return true;
        });
    }

    private void cargarVehiculos() {
        new Thread(() -> {
            lista = db.vehiculoDAO().obtenerTodos();

            runOnUiThread(() -> {
                adapter = new VehiculoAdapter(lista, this,
                        // click normal
                        (marca, modelo, año, placa, tipo_vehiculo, precio, estado, foto) -> {
                            Toast.makeText(this, marca + " " + modelo, Toast.LENGTH_SHORT).show();
                        },
                        // click eliminar
                        (vehiculo, position) -> {
                            new AlertDialog.Builder(this)
                                    .setTitle("Eliminar vehículo")
                                    .setMessage("¿Eliminar " + vehiculo.marca + " " + vehiculo.modelo + "?")
                                    .setPositiveButton("Eliminar", (dialog, which) -> {
                                        new Thread(() -> {
                                            db.vehiculoDAO().eliminar(vehiculo);
                                            runOnUiThread(() -> {
                                                lista.remove(position);
                                                adapter.notifyItemRemoved(position);
                                                Toast.makeText(this, "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                                            });
                                        }).start();
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        }
                );
                recyclerVehiculos.setAdapter(adapter);
            });
        }).start();
    }
}