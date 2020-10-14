package com.example.recety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity {


    private FirebaseAuth mAuth;
    //DatabaseReference mDatabase;
    private DatabaseReference mDatabase;

    private EditText nombre;
    private EditText correo;
    private EditText contrasena;
    private EditText contrasenaConfirmacion;
    ImageView ivFoto;
    Button btnTomarFoto;

    final int COD_FOTO = 20;
    final String CARPETA_RAIZ = "MisFotosApp";
    final String CARPETA_IMAGENES = "imagenes";
    final String RUTA_IMAGEN = CARPETA_RAIZ + CARPETA_IMAGENES;
    String path;


    public Registrarse() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);


        ivFoto = findViewById(R.id.ivFoto);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nombre = findViewById(R.id.Nombre);
        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.Contrasena);
        contrasenaConfirmacion = findViewById(R.id.ContrasenaConfirmacion);

        //permisos para android 6 o superior
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

        }

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TomarFoto();
            }
        });

    }

    public void TomarFoto(){
        String nombreImagen = "";
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();

        if(isCreada == false){
            isCreada = fileImagen.mkdirs();
        }
        if(isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }
        path =  Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        File imagen = new File(path);
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            String authorities = this.getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(this,authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));

        }
        startActivityForResult(intent,COD_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    ivFoto.setImageBitmap(bitmap);

                    break;
            }
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    public void RegistrarUsuario(View view){

        if(nombre.getText().toString().length() == 00  ||correo.getText().toString().length() == 00  ||  contrasena.getText().toString().length() == 00){
            Toast.makeText(getApplicationContext(), "Inicerte Datos.",Toast.LENGTH_SHORT).show();
        }else{

        if(contrasena.getText().toString().equals(contrasenaConfirmacion.getText().toString())){


            mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contrasena.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //ESTE ES EL MAPA DE DATOS QUE VA A INGRESAR AL NUEVO USUARIO CREADO
                                Map<String, Object> map = new HashMap<>();
                                map.put("nombre", nombre.getText().toString());
                                map.put("email", correo.getText().toString());
                                map.put("password", contrasena.getText().toString());

                                //AQUI TOMA EL ID CON EL QUE SE CREO EL USUARIO DE FIREBASE
                                String id = mAuth.getCurrentUser().getUid();

                                //ENTRA AL NODO USUARIO, LUEGO AL NOdo ID, Y AGREGA EL NUEVO USUARIO CON SUS RESPECIVOS DATOS
                                mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task2) {
                                        if(task2.isSuccessful()){
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(getApplicationContext(), "Usuario Creado",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(getApplicationContext(), "No se pudieron crear los datos",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                // If sign in fails, display a message to the user.
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(Registrarse.this, "ese usuario ya existe", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }

        }


    }
}