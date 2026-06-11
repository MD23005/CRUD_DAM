package com.example.crudapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

    @Override
    protected void onResume() {
        super.onResume();
        cargarVehiculos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        // Inicializar base de datos
        db = AppDB.getInstance(this);

        // Inicializar RecyclerView
        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        // Cargar vehículos
        cargarVehiculos();

        // Menú inferior
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_vehiculos); // marcar ítem activo

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_vehiculos) {
                return true;
            } else if (id == R.id.nav_clientes) {
                startActivity(new Intent(this, ClientesActivity.class));
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));}
//            } else if (id == R.id.nav_editar) {
//                startActivity(new Intent(this, EditarActivity.class));
//            } else if (id == R.id.nav_calendario) {
//                startActivity(new Intent(this, CalendarioActivity.class));
//            }
            return true;
        });
    }

//    private void cargarVehiculos() {
//        new Thread(() -> {
//            List<Vehiculo> lista = db.vehiculoDAO().obtenerTodos();
//
//            runOnUiThread(() -> {
//                adapter = new VehiculoAdapter(lista, this, (marca, modelo, año, placa, tipo_vehiculo, precio, estado, foto) -> {
//                    // Aquí manejas el click en un vehículo
//                    // Por ejemplo mostrar un Toast o abrir detalle
//                    Toast.makeText(this, "Vehículo: " + marca + " " + modelo, Toast.LENGTH_SHORT).show();
//                });
//                recyclerVehiculos.setAdapter(adapter);
//            });
//        }).start();
//    }

    private void cargarVehiculos() {
        new Thread(() -> {
            // Insertar datos de prueba si la BD está vacía
            List<Vehiculo> lista = db.vehiculoDAO().obtenerTodos();

            if (lista.isEmpty()) {
                db.vehiculoDAO().crear(new Vehiculo("Toyota", "Corolla", "2022", "P-123456", "Sedán", "$15,000", "Disponible", ""));
                db.vehiculoDAO().crear(new Vehiculo("Nissan", "Sentra", "2021", "P-654321", "Sedán", "$12,000", "Disponible", ""));
                db.vehiculoDAO().crear(new Vehiculo("Honda", "CR-V", "2023", "P-789456", "SUV", "$22,000", "Alquilado", ""));
                lista = db.vehiculoDAO().obtenerTodos();
            }

            final List<Vehiculo> listaFinal = lista;
            runOnUiThread(() -> {
                adapter = new VehiculoAdapter(listaFinal, this, (marca, modelo, año,
                                                                 placa, tipo_vehiculo, precio, estado, foto) -> {
                    Toast.makeText(this, marca + " " + modelo, Toast.LENGTH_SHORT).show();
                });
                recyclerVehiculos.setAdapter(adapter);
            });

            FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);
            fabAgregar.setOnClickListener(v ->
                    startActivity(new Intent(this, AgregarVehiculoActivity.class))
            );



        }).start();
    }


}