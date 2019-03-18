package com.example.subidaproductos.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.subidaproductos.Entidades.Cliente;
import com.example.subidaproductos.MainActivity;
import com.example.subidaproductos.R;

public class CrearCliente extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtTelefono;
    private EditText edtDireccion;
    private boolean trazado;
    private Button btnCrear;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        edtNombre = findViewById(R.id.edt_nom_cliente);
        edtTelefono = findViewById(R.id.edt_tel_cliente);
        edtDireccion = findViewById(R.id.edt_dir_cliente);
        btnCrear = findViewById(R.id.btn_crear_nuevocliente);
        btnCancelar = findViewById(R.id.btn_cancelar_nuevocliente);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombre.getText().toString().isEmpty() || edtTelefono.getText().toString().isEmpty()){
                    Toast.makeText(CrearCliente.this, "Datos insificientes", Toast.LENGTH_SHORT).show();
                }else{
                    String nombre = edtNombre.getText().toString();
                    String telefono = edtTelefono.getText().toString();
                    String direccion = edtDireccion.getText().toString();

                    MainActivity.cliente = new Cliente(nombre,telefono,direccion,false);
                    startActivity(new Intent(CrearCliente.this,MainActivity.class));
                    Toast.makeText(CrearCliente.this, "Cliente creado", Toast.LENGTH_SHORT).show();





                }

            }
        });
    }
}
