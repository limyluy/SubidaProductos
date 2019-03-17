package com.example.subidaproductos.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.subidaproductos.Entidades.Cliente;
import com.example.subidaproductos.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ClienteAdaptador extends FirestoreRecyclerAdapter<Cliente, ClienteAdaptador.ClienteViewHolder> {
    Context  context;
    OnItemClickListener listener;

    public ClienteAdaptador(@NonNull FirestoreRecyclerOptions<Cliente> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ClienteViewHolder holder, int position, @NonNull Cliente model) {
        holder.nombre.setText(model.getNombre());
        holder.descripcion.setText("numero de locales: ");
        holder.numero.setText(String.valueOf(model.getLocales().size()));
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_main,viewGroup,false);
        return new ClienteViewHolder(v);
    }

    class ClienteViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView descripcion;
        TextView numero;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.txt_nombre_item);
            descripcion = itemView.findViewById(R.id.txt_detalle_item);
            numero = itemView.findViewById(R.id.txt_numero_item);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
