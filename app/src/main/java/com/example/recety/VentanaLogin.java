package com.example.recety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private TextView mTextViewResult;
    private RequestQueue mQueue;
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

        mTextViewResult = findViewById(R.id.text_view_result);
     mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
       TextView buttonParse = findViewById(R.id.button_parse);
       mQueue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
            }
        });


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

    private void jsonParse(){
        String url="https://invessoft.com/api/eventos/1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("agenda");
                    for (int i = 0; i < jsonArray.length(); i++ ){
                        JSONObject agend = jsonArray.getJSONObject(i);

                        int id_trabajo_agenda = agend.getInt("id_trabajo_agenda");
                        String Fecha = agend.getString("fecha");
                        String hora_inicio = agend.getString("hora_inicio");
                        String hora_fin = agend.getString("hora_fin");
                        int id_espacio = agend.getInt("id_espacio");



                        mTextViewResult.append("Id Trabajo Agenda: " + String.valueOf(id_trabajo_agenda) + ", " + "Fecha: " + Fecha + ", " + "Hora Inicio: " + hora_inicio + ", " + "Hora Fin: " + hora_fin + ", " + "Id Espacio: " + String.valueOf(id_espacio) + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }
}