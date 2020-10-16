package com.example.recety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecuperarContrasena extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
        getSupportActionBar().hide();


        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.Correo);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    public void EnviarCorreo(View View){
        String email = correo.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Ingrese su correo electrónico registrado", Toast.LENGTH_SHORT).show();
            return;
        }else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RecuperarContrasena.this, "¡Le hemos enviado instrucciones para restablecer su contraseña!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), IniciarSesion.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(RecuperarContrasena.this, "Correo invalido", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
