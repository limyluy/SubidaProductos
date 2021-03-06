package com.example.subidaproductos.Actividades;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subidaproductos.Entidades.Cliente;
import com.example.subidaproductos.Entidades.Local;
import com.example.subidaproductos.MainActivity;
import com.example.subidaproductos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CrearLocal extends AppCompatActivity {

    private static final int IMAGEN_PUESTA = 1;
    private static final int IMAGEN_PUESTA_LOGO = 2;

    private EditText edtNombre;
    private EditText edtDescripcion;
    private EditText edtTelefono;
    private EditText edtDireccion;
    private CheckBox chbGarage;
    private CheckBox chbTarjeta;
    private CheckBox chbGarantia;
    private Button btnUbicacion;
    private Button btnImgLocal;
    private Button btnImgLogo;
    private EditText edtEtiquetas;
    private Button btnAddEtiquetas;
    private TextView txtEtiquetas;
    private ImageView imgLocal;
    private ImageView imgLogo;

    private double longitud;
    private double latitud;
    private Uri imgLocalUri;
    private Uri imgLogoUri;

    private List<String> etiquetaRes = new ArrayList<>();
    private List<String> clienteRes = new ArrayList<>();
    private boolean locationPrendida;
    private boolean trazado;
    private Button btnCrear;
    private Button btnCancelar;
    LocationManager locationManager;

    private StorageReference mStoraRef;
    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_local);

        edtNombre = findViewById(R.id.edt_nom_local);
        edtDescripcion = findViewById(R.id.edt_des_local);
        edtTelefono = findViewById(R.id.edt_tel_local);
        edtDireccion = findViewById(R.id.edt_dir_local);
        chbGarage = findViewById(R.id.chb_garage_local);
        chbTarjeta = findViewById(R.id.chb_tarjeta_local);
        chbGarantia = findViewById(R.id.chb_garantia_local);
        btnUbicacion = findViewById(R.id.btn_ubicacion_local);
        btnImgLocal = findViewById(R.id.btn_img_local);
        btnImgLogo = findViewById(R.id.btn_img_logo);
        edtEtiquetas = findViewById(R.id.edt_etiquetas_local);
        btnAddEtiquetas = findViewById(R.id.btn_add_etiquetas);
        txtEtiquetas = findViewById(R.id.txt_etiquetas);
        btnCrear = findViewById(R.id.btn_crear_nuevolocal);
        btnCancelar = findViewById(R.id.btn_cancelar_nuevolocal);
        imgLocal = findViewById(R.id.img_local);
        imgLogo = findViewById(R.id.img_logo);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mStoraRef = FirebaseStorage.getInstance().getReference("uploads");

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombre.getText().toString().isEmpty() || edtTelefono.getText().toString().isEmpty() || edtDescripcion.getText().toString().isEmpty()) {
                    Toast.makeText(CrearLocal.this, "Datos insificientes", Toast.LENGTH_SHORT).show();
                } else {
                    crearLocalNuevo();
                    startActivity(new Intent(CrearLocal.this,MainActivity.class));
                }

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrearLocal.this,MainActivity.class));
            }
        });

        btnImgLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscadorImagen(IMAGEN_PUESTA);
            }
        });

        btnImgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscadorImagen(IMAGEN_PUESTA_LOGO);
            }
        });

        btnAddEtiquetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarEtiquetas();
            }
        });
    }

    private void llenarEtiquetas() {
        if (edtEtiquetas.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo basio", Toast.LENGTH_SHORT).show();
        }else{
            etiquetaRes.add(edtEtiquetas.getText().toString());
            mostrarEtiquetas();
            edtEtiquetas.setText("");
        }
    }

    private void mostrarEtiquetas() {

        String resultados = "";
        for (int i = 0; i < etiquetaRes.size(); i++)
            if(i + 1 < etiquetaRes.size())
                resultados += etiquetaRes.get(i) + " | ";
            else
                resultados += etiquetaRes.get(i);

        txtEtiquetas.setText(resultados);
    }

    private void buscadorImagen(int codImagen) {
        Intent intent =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,codImagen);
    }

    private void crearLocalNuevo() {
        String nombre = edtNombre.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String direccion;
        String descripcion = edtDescripcion.getText().toString();
        Boolean garage;
        Boolean tarjeta;
        Boolean garantia;
        GeoPoint ubicacion;
        String uriImgLocal;
        String iruImgLogo;
        List<String> etiquetas;
        List<String> clientes = new ArrayList<>();
        Boolean actualizado = true;
        long numRcomendados = 0;
        int atencion = 0;
        int calidad = 0;
        int precio = 0;


        if (!isLocationEnabled()) {
            showAlert(); return;
        }else{ubicacion = obtenerGeoPoint();}

        if (ubicacion == null) {
            Toast.makeText(CrearLocal.this, "No localizacion", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtDireccion.getText().toString().isEmpty()){
            direccion = "Solo Contacto";
        }else {
            direccion = edtDireccion.getText().toString();
        }
        garage = chbGarage.isActivated();
        tarjeta = chbTarjeta.isActivated();
        garantia =chbGarantia.isActivated();

        if (imgLogoUri == null || imgLocalUri == null){
            Toast.makeText(this, "no imagen seleccionada", Toast.LENGTH_SHORT).show();
            uriImgLocal = "no imagen";
            iruImgLogo = "no imagen";
        }else{
           uriImgLocal = cargarImages(imgLocalUri);
           iruImgLogo = cargarImages(imgLogoUri);
        }

        if (etiquetaRes.isEmpty()){
            etiquetas = null;
        }else{
            etiquetas = etiquetaRes;
        }

        clientes = null;


        Local localCreado = new Local(nombre,descripcion,ubicacion,atencion,calidad,precio,telefono,tarjeta,garage,garantia,uriImgLocal,iruImgLogo,0,true,clientes,etiquetas);

        MainActivity.local = localCreado;



    }

    private String cargarImages(Uri uri) {
        StorageReference Reference = mStoraRef.child(System.currentTimeMillis() +"."
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
                Toast.makeText(CrearLocal.this, "Error al Cargar", Toast.LENGTH_SHORT).show();

            }
        });

        return direccion;
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


    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGEN_PUESTA && resultCode ==   RESULT_OK
        && data != null && data.getData() != null){

            imgLocalUri =data.getData();

            Picasso.with(this).load(imgLocalUri).into(imgLocal);

        }

        if (requestCode == IMAGEN_PUESTA_LOGO && resultCode == RESULT_OK
        && data != null && data.getData() != null){

            imgLogoUri = data.getData();

            Picasso.with(this).load(imgLogoUri).into(imgLogo);
        }
    }

    private String getFileExtencion( Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
