package com.example.subidaproductos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.subidaproductos.Adaptadores.ClienteAdaptador;
import com.example.subidaproductos.Entidades.Cliente;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    Button btnNuevoCliente;
    Context  context;
    FirebaseFirestore db;


    ClienteAdaptador adaptadorCliente;
    RecyclerView rcvMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       db = FirebaseFirestore.getInstance();

        btnNuevoCliente = findViewById(R.id.btn_crear_cliente);
        context = getApplicationContext();

        rcvMain = findViewById(R.id.rcv_main);
        rcvMain.setHasFixedSize(true);
        rcvMain.setLayoutManager(new LinearLayoutManager(this));

        CollectionReference reference = db.collection("clientes");

        Query query = reference.orderBy("nombre", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Cliente> options = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query,Cliente.class)
                .build();

        adaptadorCliente = new ClienteAdaptador(options,context);

        btnNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verClientesBaseDatos();
            }
        });

    }

    private void verClientesBaseDatos() {
        rcvMain.setAdapter(adaptadorCliente);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adaptadorCliente.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptadorCliente.stopListening();
    }
}
