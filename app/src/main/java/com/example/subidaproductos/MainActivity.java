package com.example.subidaproductos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subidaproductos.Actividades.CrearCliente;
import com.example.subidaproductos.Actividades.CrearLocal;
import com.example.subidaproductos.Actividades.CrearProducto;
import com.example.subidaproductos.Adaptadores.ClienteAdaptador;
import com.example.subidaproductos.Entidades.Cliente;
import com.example.subidaproductos.Entidades.Local;
import com.example.subidaproductos.Entidades.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txtNomCliente;
    TextView txtNomLocal;
    TextView txtNomProducto;
    Button btnNuevoCliente;
    Button btnEscogerClientes;
    Button btnNuevoLocal;
    Button btnSubir;
    Button btnNuevoProducto;
    Context context;
    FirebaseFirestore db;
    String idCliente;

    public static Cliente cliente;
    public static Local local;
    public static Producto producto;


    ClienteAdaptador adaptadorCliente;
    RecyclerView rcvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtNomCliente = findViewById(R.id.txt_nocliente);
        txtNomLocal = findViewById(R.id.txt_nolocal);
        txtNomProducto = findViewById(R.id.txt_noproducto);
        btnNuevoCliente = findViewById(R.id.btn_crear_cliente);
        btnEscogerClientes = findViewById(R.id.btn_escojer_cliente);
        btnNuevoLocal = findViewById(R.id.btn_crear_local);
        btnNuevoProducto = findViewById(R.id.btn_crear_producto);
        btnSubir = findViewById(R.id.btn_subir);


        db = FirebaseFirestore.getInstance();
        context = getApplicationContext();

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

        btnNuevoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CrearLocal.class));
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirDatosFirestore();
            }
        });

        btnNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CrearProducto.class));
            }
        });

    }

    private void subirDatosFirestore() {
        if (cliente == null && local == null) {
            Toast.makeText(MainActivity.this, "Datos insificientes", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cliente != null && idCliente == null) {

            clienteNuevo();

        }
        if (cliente != null && idCliente != null && local != null) {

            localNuevoClienteViejo();
        }


    }

    private void localNuevoClienteViejo() {
        // metodo para crear local con cliente ya exixstente

    }

    private void clienteNuevo() {

        db.collection("clientes").document().set(cliente).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, cliente.getNombre() + " cargado", Toast.LENGTH_SHORT).show();
                cliente = null;
                txtNomCliente.setText("No cliente");
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, "no realzado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void irllenandoDatos() {


        if (cliente != null) txtNomCliente.setText(cliente.getNombre());
        if (local != null)txtNomLocal.setText(local.getNombre());
        if (producto != null)txtNomProducto.setText(producto.getNombre());
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
                idCliente = documentSnapshot.getId();
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
