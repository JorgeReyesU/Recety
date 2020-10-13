package com.example.recety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


    public Registrarse() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nombre = findViewById(R.id.Nombre);
        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.Contrasena);
        contrasenaConfirmacion = findViewById(R.id.ContrasenaConfirmacion);

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