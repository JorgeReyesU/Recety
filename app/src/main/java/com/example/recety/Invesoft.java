package com.example.recety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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

public class Invesoft extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invesoft);

        mTextViewResult = findViewById(R.id.textResult);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
        TextView buttonParse = findViewById(R.id.buttonText);
        mQueue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
            }
        });
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

    public  void volver(View v){
        //navegar
        Intent irInv = new Intent(this, VentanaLogin.class);
        //flags
        irInv.addFlags(irInv.FLAG_ACTIVITY_CLEAR_TOP | irInv.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(irInv);

    }

}