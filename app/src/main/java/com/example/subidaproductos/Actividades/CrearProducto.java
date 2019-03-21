package com.example.subidaproductos.Actividades;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subidaproductos.Entidades.Producto;
import com.example.subidaproductos.MainActivity;
import com.example.subidaproductos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CrearProducto extends AppCompatActivity {

    private static final int IMAGEN_RESULTADO = 1;

    private EditText edtNombre;
    private EditText edtMarca;
    private EditText edtDescripcion;
    private EditText edtInfoAdicional;
    private EditText edtDesAdicional;
    private CheckBox chbOferta;
    private EditText edtEtiquetas;
    private TextView txtEtiquetas;
    private ImageView imgProducto;
    private Button btnImgProducto;
    private Button btnAddEtiquetas;
    private Button btnCrearProducto;
    private Button btnCancelar;


    private List<String> etiquetasArreglo = new ArrayList<>();
    private Uri uriImgProducto;
    private StorageReference mREf;
    private StorageTask mUploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        edtNombre = findViewById(R.id.edt_nom_producto);
        edtMarca = findViewById(R.id.edt_marca_producto);
        edtDescripcion = findViewById(R.id.edt_des_producto);
        edtInfoAdicional = findViewById(R.id.edt_infoadicional_producto);
        edtDesAdicional = findViewById(R.id.edt_desadicional_producto);
        chbOferta = findViewById(R.id.chb_oferta_producto);
        imgProducto = findViewById(R.id.img_producto);
        edtEtiquetas = findViewById(R.id.edt_etiquetas_producto);
        txtEtiquetas = findViewById(R.id.txt_etiquetas_producto);
        btnImgProducto = findViewById(R.id.btn_img_producto);
        btnAddEtiquetas = findViewById(R.id.btn_add_etiquetas);
        btnCrearProducto = findViewById(R.id.btn_crear_nuevo_producto);
        btnCancelar = findViewById(R.id.btn_cancelar_nuevoproducto);

        mREf = FirebaseStorage.getInstance().getReference("productos");


        btnCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProducto();
            }
        });

        btnAddEtiquetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarEtiquetas();
            }
        });

        btnImgProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscadorImagen(IMAGEN_RESULTADO);
            }
        });


    }

    private void llenarEtiquetas() {
        if (edtEtiquetas.getText().toString().isEmpty()) {
            Toast.makeText(this, "Eqtiqueta no valida", Toast.LENGTH_SHORT).show();
        } else {

            String etiquetaRes = edtEtiquetas.getText().toString();
            etiquetasArreglo.add(etiquetaRes);
            mostrarEtiquetas();
            edtEtiquetas.setText("");
        }
    }

    private void crearProducto() {
        if (edtNombre.getText().toString().isEmpty() || edtDescripcion.getText().toString().isEmpty()) {
            Toast.makeText(this, "Informacion insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre;
        String descripcion;
        String marca = "";
        String infoadicional = "";
        String desAdicional = "";
        String imgProdcuto = null;
        boolean oferta;
        List<String> etiquetas = null;

        nombre = edtNombre.getText().toString();
        descripcion = edtDescripcion.getText().toString();

        if (!(edtMarca.getText().toString().isEmpty())) {
            marca = edtMarca.getText().toString();
        }
        if (!(edtInfoAdicional.getText().toString().isEmpty())) {
            infoadicional = edtInfoAdicional.getText().toString();
        }
        if (!(edtDesAdicional.getText().toString().isEmpty())) {
            desAdicional = edtDesAdicional.getText().toString();
        }

        oferta = chbOferta.isActivated();
        etiquetas = etiquetasArreglo;
        if (uriImgProducto == null){
            Toast.makeText(this, "Es necesario Imagen", Toast.LENGTH_SHORT).show();
        }else{
            imgProdcuto = cargarImages(uriImgProducto);
        }

        Producto  producto = new Producto(nombre,descripcion,marca,imgProdcuto,infoadicional,desAdicional,oferta,etiquetas,null);

        MainActivity.producto = producto;

        startActivity(new Intent(CrearProducto.this,MainActivity.class));


    }

    private void mostrarEtiquetas() {

        String resultados = "";
        for (int i = 0; i < etiquetasArreglo.size(); i++)
            if (i + 1 < etiquetasArreglo.size())
                resultados += etiquetasArreglo.get(i) + " | ";
            else
                resultados += etiquetasArreglo.get(i);

        txtEtiquetas.setText(resultados);
    }

    private void buscadorImagen(int codImagen) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, codImagen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGEN_RESULTADO && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            uriImgProducto = data.getData();

            Picasso.with(this).load(uriImgProducto).into(imgProducto);

        }

    }

    private String cargarImages(Uri uri) {
        StorageReference Reference = mREf.child(System.currentTimeMillis() +"."
                +getFileExtencion(uri));
        String direccion = null;

        mUploadTask = Reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful());
                Uri uri1 = (Uri) task.getResult();

                String direccion = uri1.toString();




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearProducto.this, "Error al Cargar", Toast.LENGTH_SHORT).show();

            }
        });

        return direccion;
    }

    private String getFileExtencion( Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
