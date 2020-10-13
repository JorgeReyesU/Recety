package com.example.recety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VentanaLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_login);


    }


    public void irIniciar(View view){
        Intent i = new Intent(this, IniciarSesion.class);
        startActivity(i);
    }

    public void irRegistrar(View view){
            Intent i = new Intent(this, Registrarse.class);
            startActivity(i);
    }
}