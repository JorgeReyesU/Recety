package com.example.recety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VentanaLogin extends AppCompatActivity {

    TextView IrHome, IrRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_login);
        getSupportActionBar().hide();
        IrHome = findViewById(R.id.IniciarSinCuenta);
        IrRegistrarse = findViewById(R.id.iniciarRegistrarse);
        IrHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                irHome();
            }

        });
        IrRegistrarse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                irRegistrar();
            }

        });

    }

    public  void irInvesoft(View v){
        //navegar
        Intent irInv = new Intent(this, Invesoft.class);
        //flags
        irInv.addFlags(irInv.FLAG_ACTIVITY_CLEAR_TOP | irInv.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(irInv);

    }


    public void irIniciar(View view){
        Intent i = new Intent(this, IniciarSesion.class);
        startActivity(i);
    }

    public void irRegistrar(){
            Intent i = new Intent(this, Registrarse.class);
            startActivity(i);
    }

    public void irHome(){
        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
    }

}