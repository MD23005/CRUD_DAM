package com.example.crudapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.R;
import com.example.crudapplication.entities.Vehiculo;
import androidx.annotation.NonNull;
import java.util.List;

public class VehiculoAdapter  extends
        RecyclerView.Adapter<VehiculoAdapter.TareaViewHolder> {
    List<Vehiculo> lista;
    private Context miContext;
    final private clickLista miClick;

    public VehiculoAdapter(List<Vehiculo> lista, Context context, clickLista
            listener) {
        this.lista = lista;
        miContext = context;
        miClick = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View vista =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.vehiculo_item_layout, parent, false);
        return new TareaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int
            position) {
        Vehiculo vehiculo = lista.get(position);
        holder.marca.setText(vehiculo.marca);
        holder.modelo.setText(vehiculo.modelo);
        holder.año.setText(vehiculo.año);
        holder.placa.setText(vehiculo.placa);
        holder.tipo_vehiculo.setText(vehiculo.tipo_vehiculo);
        holder.precio.setText(vehiculo.precio);
        holder.estado.setText(vehiculo.estado);
        //holder.foto.setText(vehiculo.foto);
       // holder.foto.setImageResource(R.drawable.ic_launcher_background);


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView marca, modelo, año, placa, tipo_vehiculo, precio, estado, foto;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            marca = itemView.findViewById(R.id.tvMarca);
            modelo = itemView.findViewById(R.id.tvModelo);
            año = itemView.findViewById(R.id.tvAño);
            placa = itemView.findViewById(R.id.tvPlaca);
            tipo_vehiculo = itemView.findViewById(R.id.tvTipo_Vehiculo);
            precio = itemView.findViewById(R.id.tvPrecio);
            estado = itemView.findViewById(R.id.tvEstado);
            //foto = itemView.findViewById(R.id.tvFoto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPresionado = getAdapterPosition();
//            int itemPresionado=getAbsoluteAdapterPosition();
            String _marca = marca.getText().toString();
            String _modelo = modelo.getText().toString();
            String _año = año.getText().toString();
            String _placa = placa.getText().toString();
            String _tipo_vehiculo = tipo_vehiculo.getText().toString();
            String _precio = precio.getText().toString();
            String _estado = estado.getText().toString();
            String _foto = foto.getText().toString();
            miClick.clickItem(_marca, _modelo, _año, _placa, _tipo_vehiculo, _precio, _estado, _foto);
        }
    }

    public interface clickLista {
        void clickItem(String marca, String modelo, String año, String placa, String tipo_Vehiculo, String precio, String estado, String foto);
    }
}
