package com.example.crudapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.R;
import com.example.crudapplication.entities.AlquilarVehiculo; // Asegúrate de que esta sea la ruta de tu entidad

import java.util.List;

public class AlquilerAdapter extends RecyclerView.Adapter<AlquilerAdapter.AlquilerViewHolder> {

    private List<AlquilarVehiculo> listaAlquileres;
    private Context context;

    // Constructor del adaptador
    public AlquilerAdapter(List<AlquilarVehiculo> listaAlquileres, Context context) {
        this.listaAlquileres = listaAlquileres;
        this.context = context;
    }

    @NonNull
    @Override
    public AlquilerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el archivo de texto limpio item_alquiler.xml que creamos anteriormente
        View vista = LayoutInflater.from(context).inflate(R.layout.alquiler_item, parent, false);
        return new AlquilerViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AlquilerViewHolder holder, int position) {
        // Obtenemos el objeto alquiler de la posición actual
        AlquilarVehiculo alquiler = listaAlquileres.get(position);

        // Pasamos los datos a los componentes de texto del XML
        // Nota: Si tus IDs son numéricos (int), recuerda concatenar "" para evitar que Android busque un recurso inexistente
        holder.tvIdAlquiler.setText("Alquiler #" + alquiler.getID_Alquiler());
        holder.tvIdAuto.setText("Vehículo ID: #" + alquiler.getID_Auto());
        holder.tvIdCliente.setText("Cliente ID: #" + alquiler.getID_Cliente());
        holder.tvFechaInicio.setText(alquiler.getFecha_Inicio());
        holder.tvFechaFin.setText(alquiler.getFecha_Fin());
    }

    @Override
    public int getItemCount() {
        // Retorna el tamaño total de la lista; si es nula, retorna 0 para evitar fallos
        return listaAlquileres != null ? listaAlquileres.size() : 0;
    }

    // Clase interna ViewHolder que mapea los elementos visuales del archivo XML
    public static class AlquilerViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdAuto, tvIdAlquiler, tvIdCliente, tvFechaInicio, tvFechaFin;

        public AlquilerViewHolder(@NonNull View itemView) {
            super(itemView);

            // Vinculamos las variables con los IDs exactos de item_alquiler.xml
            tvIdAuto = itemView.findViewById(R.id.tvIdAuto);
            tvIdAlquiler = itemView.findViewById(R.id.tvIdAlquiler);
            tvIdCliente = itemView.findViewById(R.id.tvIdCliente);
            tvFechaInicio = itemView.findViewById(R.id.tvFechaInicio);
            tvFechaFin = itemView.findViewById(R.id.tvFechaFin);
        }
    }
}