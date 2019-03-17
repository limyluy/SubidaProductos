package com.example.subidaproductos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subidaproductos.Actividades.CrearCliente;
import com.example.subidaproductos.Adaptadores.ClienteAdaptador;
import com.example.subidaproductos.Entidades.Cliente;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txtNomCliente;
    Button btnNuevoCliente;
    Button btnEscogerClientes;
    Context context;
    FirebaseFirestore db;

    public static Cliente cliente;


    ClienteAdaptador adaptadorCliente;
    RecyclerView rcvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        db = FirebaseFirestore.getInstance();
        context = getApplicationContext();



        txtNomCliente = findViewById(R.id.txt_nocliente);
        btnNuevoCliente = findViewById(R.id.btn_crear_cliente);
        btnEscogerClientes = findViewById(R.id.btn_escojer_cliente);


        rcvMain = findViewById(R.id.rcv_main);
        rcvMain.setHasFixedSize(true);
        rcvMain.setLayoutManager(new LinearLayoutManager(this));
        irllenandoDatos();
        inicializarAdaptadorClientes();

        btnEscogerClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verClientesBaseDatos();
            }
        });

        btnNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CrearCliente.class));

            }
        });

    }

    private void irllenandoDatos() {


        if (cliente != null)txtNomCliente.setText(cliente.getNombre());
    }

    private void inicializarAdaptadorClientes() {

        CollectionReference referenceCliente = db.collection("clientes");
        Query query = referenceCliente.orderBy("nombre", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Cliente> options = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adaptadorCliente = new ClienteAdaptador(options, context);

    }

    private void verClientesBaseDatos() {
        rcvMain.setAdapter(adaptadorCliente);
        adaptadorCliente.setOnItemClickListener(new ClienteAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                cliente = documentSnapshot.toObject(Cliente.class);
                String nombre = cliente.getNombre();
                txtNomCliente.setText(nombre);
                Toast.makeText(context, nombre + " escogido", Toast.LENGTH_SHORT).show();
            }
        });

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
