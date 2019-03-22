package com.example.subidaproductos.Actividades;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subidaproductos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CrearEvento extends AppCompatActivity {

    private static final int IMAGEN_PUESTA = 1;

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

    private GeoPoint geoPoint;

    double longitud;
    double latitud;
    private List<String> categoriasArray = new ArrayList<>();
    LocationManager locationManager;


    private StorageReference mStoraRef;
    private StorageTask mUploadTask;

    private Uri uno;
    private Uri dos;
    private Uri tres;


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

        mStoraRef = FirebaseStorage.getInstance().getReference("eventos");


        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crerEvento();
            }
        });
        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoPoint = obtenerGeoPoint();
            }
        });
        btnAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarEtiquetas();
                mostrarEtiquetas();
            }
        });
        btnImgSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscadorImagen(IMAGEN_PUESTA);
            }
        });

    }

    private void crerEvento() {

        if (edtNombre.getText().toString().isEmpty() || edtDescripcion.getText().toString().isEmpty() || geoPoint == null) {
            Toast.makeText(this, "Datos insuficientes", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre;
        String descripcion;
        GeoPoint ubicacion;
        String fecha;
        String desAdicional;
        String especificaciones;
        List<String> categoria;
        List<String> fotos;
        List<String> locales;

        if (!isLocationEnabled()) {
            showAlert();
            return;
        } else {
            ubicacion = obtenerGeoPoint();
        }

        nombre = edtNombre.getText().toString();
        descripcion = edtDescripcion.getText().toString();
        ubicacion = geoPoint;
        fecha =;
        if (edtDesAdicional.getText().toString().isEmpty()) {
            desAdicional = "";
        } else {
            desAdicional = edtDesAdicional.getText().toString();
        }

        if (edtespecificaicones.getText().toString().isEmpty()) {
            especificaciones = "";
        } else {
            especificaciones = edtespecificaicones.getText().toString();
        }

        categoria = categoriasArray;


    }

    private void buscadorImagen(int codImagen) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, codImagen);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private String cargarImages(Uri uri) {
        StorageReference Reference = mStoraRef.child(System.currentTimeMillis() + "."
                + getFileExtencion(uri));
        String direccion = null;

        mUploadTask = Reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful()) ;
                Uri uri1 = (Uri) task.getResult();

                String direccion = uri1.toString();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearEvento.this, "Error al Cargar", Toast.LENGTH_SHORT).show();

            }
        });

        return direccion;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (uno == null) {
            if (requestCode == IMAGEN_PUESTA && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {

                uno = data.getData();

                Picasso.with(this).load(uno).into(imgUno);

            }
        }

        if (uno != null && dos == null) {
            if (requestCode == IMAGEN_PUESTA && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {

                dos = data.getData();

                Picasso.with(this).load(dos).into(imgDos);

            }

        }

        if (uno != null && dos != null && tres == null) {
            if (requestCode == IMAGEN_PUESTA && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {

                tres = data.getData();

                Picasso.with(this).load(tres).into(imgTres);

            }

        }

        if (uno != null && dos != null && tres == null) {
            if (requestCode == IMAGEN_PUESTA && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {

                tres = data.getData();

                Picasso.with(this).load(tres).into(imgTres);

            }

        }

        if (uno != null && dos != null && tres != null) {
            if (requestCode == IMAGEN_PUESTA && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {

                uno = data.getData();

                Picasso.with(this).load(uno).into(imgUno);

            }

        }

    }

    private String getFileExtencion(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private GeoPoint obtenerGeoPoint() {

        LocationListener listener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {

                longitud = location.getLongitude();
                latitud = location.getLatitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        GeoPoint geoPoint = new GeoPoint(latitud, longitud);


        return geoPoint;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    private void mostrarEtiquetas() {

        String resultados = "";
        for (int i = 0; i < categoriasArray.size(); i++)
            if (i + 1 < categoriasArray.size())
                resultados += categoriasArray.get(i) + " | ";
            else
                resultados += categoriasArray.get(i);

        txtCategorias.setText(resultados);
    }

    private void llenarEtiquetas() {
        if (edtCategoria.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campo basio", Toast.LENGTH_SHORT).show();
        } else {
            categoriasArray.add(edtCategoria.getText().toString());
            mostrarEtiquetas();
            edtCategoria.setText("");
        }
    }

}
