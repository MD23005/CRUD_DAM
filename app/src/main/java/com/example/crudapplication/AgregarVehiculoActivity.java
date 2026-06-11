package com.example.crudapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crudapplication.data.AppDB;
import com.example.crudapplication.entities.Vehiculo;

public class AgregarVehiculoActivity extends AppCompatActivity {

    EditText etMarca, etModelo, etAño, etPlaca, etPrecio;
    ImageView imgFoto;
    Spinner spinnerTipoVehiculo;
    Spinner spinnerEstado;
    Button btnSeleccionarFoto, btnGuardar, btnCancelar;
    AppDB db;
    String rutaFoto = ""; // guarda la URI de la foto seleccionada

    // Launcher para abrir la galería
    ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    // Mantiene imagen al cerrar  y abrir app
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                    imgFoto.setImageURI(uri);
                    rutaFoto = uri.toString(); // guarda el String en BD
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vehiculo);

        db = AppDB.getInstance(this);

        // Vincular vistas
        imgFoto = findViewById(R.id.imgFoto);
        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etAño = findViewById(R.id.etAño);
        etPlaca = findViewById(R.id.etPlaca);
        etPrecio = findViewById(R.id.etPrecio);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        spinnerEstado = findViewById(R.id.spinnerEstado);

        btnCancelar.setOnClickListener(v -> cerrar());

        String[] tipos = {"Sedán", "SUV", "Pick-up", "Camión", "Van", "Motocicleta", "Deportivo", "Exotico"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tipos
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoVehiculo.setAdapter(adapterSpinner);


        // Define la lista de estados
        String[] estados = {"Disponible", "Alquilado", "En mantenimiento", "No disponible"};

        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ← "adapterEstado" no "adapterSpinner"
        spinnerEstado.setAdapter(adapterEstado);

        // Abrir galería
        btnSeleccionarFoto.setOnClickListener(v ->
                seleccionarImagenLauncher.launch("image/*")
        );

        // Guardar vehículo
        btnGuardar.setOnClickListener(v -> guardarVehiculo());
    }
    void cerrar(){
        finish();
    }
    private void guardarVehiculo() {
        String marca = etMarca.getText().toString().trim();
        String modelo = etModelo.getText().toString().trim();
        String año = etAño.getText().toString().trim();
        String placa = etPlaca.getText().toString().trim();
        String tipo = spinnerTipoVehiculo.getSelectedItem().toString();
        String precio = etPrecio.getText().toString().trim();
        String estado = spinnerEstado.getSelectedItem().toString();

        // Validar que los campos no estén vacíos
        if (marca.isEmpty() || modelo.isEmpty() || año.isEmpty() ||
                placa.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Vehiculo nuevo = new Vehiculo(marca, modelo, año, placa, tipo, precio, estado, rutaFoto);

        new Thread(() -> {
            db.vehiculoDAO().crear(nuevo);
            runOnUiThread(() -> {
                Toast.makeText(this, "Vehículo guardado correctamente", Toast.LENGTH_SHORT).show();
                finish(); // cierra esta pantalla y regresa a VehiculosActivity
            });
        }).start();


    }
}