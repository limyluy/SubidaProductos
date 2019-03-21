package com.example.subidaproductos.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.subidaproductos.R;

public class CrearEvento extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtDescripcion;
    private EditText edtDesAdicional;
    private EditText edtespecificaicones;
    private EditText edtCategoria;
    private Button btnImgSerach;
    private ImageView imgUno;
    private ImageView imgDos;
    private ImageView imgTres;
    private Button btnFecha;
    private Button btnUbicacion;
    private Button btnAddCategoria;
    private TextView txtCategorias;
    private Button btnCrear;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        edtNombre = findViewById(R.id.edt_nom_evento);
        edtDescripcion = findViewById(R.id.edt_des_evento);
        edtDesAdicional = findViewById(R.id.edt_desadicional_evento);
        edtespecificaicones = findViewById(R.id.edt_especificaciones_evento);
        edtCategoria = findViewById(R.id.edt_etiquetas_evento);
        btnImgSerach = findViewById(R.id.btn_img_evento);
        imgUno = findViewById(R.id.img_evento);
        imgDos = findViewById(R.id.img_evento1);
        imgTres = findViewById(R.id.img_evento2);
        btnFecha = findViewById(R.id.btn_fecha_evento);
        btnUbicacion = findViewById(R.id.btn_add_categoria);
        btnAddCategoria = findViewById(R.id.btn_add_categoria);
        txtCategorias = findViewById(R.id.txt_categorias_evento);
        btnCrear = findViewById(R.id.btn_crear_nuevoevento);
        btnCancelar = findViewById(R.id.btn_cancelar_nuevoevento);

    }
}
