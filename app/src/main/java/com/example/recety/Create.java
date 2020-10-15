package com.example.recety;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recety.model.Receta;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Create#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Create extends Fragment {
    private List<Receta> listR = new ArrayList<Receta>();
    ArrayAdapter<Receta> arrayAdapterR;
    TextView receta,descripcion;
    private Button btnGuardarReceta;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView correo;
    ListView listV_personas;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Create() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Create.
     */
    // TODO: Rename and change types and number of parameters
    public static Create newInstance(String param1, String param2) {
        Create fragment = new Create();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create2, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnGuardarReceta = v.findViewById(R.id.btnGuardar);
        receta = v.findViewById(R.id.txt_NombreReceta);
        descripcion = v.findViewById(R.id.txt_DescripcionReceta);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUser userfire = FirebaseAuth.getInstance().getCurrentUser();
        final String idgoogle = userfire.getUid();
        final String idfire = user.getUid();


        if(user != null || userfire != null){
            btnGuardarReceta.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //ESTE ES EL MAPA DE DATOS QUE VA A INGRESAR AL NUEVO USUARIO CREADO
                    FirebaseUser userfire = FirebaseAuth.getInstance().getCurrentUser();
                    String id;


                    id = UUID.randomUUID().toString();
                    String idreceta = "" + id;
                    String nombrereceta = receta.getText().toString();
                    String descripcionreceta = descripcion.getText().toString();
                    String idusuariogoogle = idgoogle.toString();
                    String idusuariofire = idfire.toString();
                    Map<String, Object> map = new HashMap<>();


                    map.put("NombreReceta", nombrereceta);
                    map.put("Descripcion", descripcionreceta);
                    map.put("idusuariogoogle", idusuariogoogle);
                    map.put("idusuariofire", idusuariofire);


                    mDatabase.child("Recetas").child(idreceta).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getActivity().getApplicationContext(), "Receta guardada",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "no se guardo receta ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(getActivity().getApplicationContext(), "GUARDADA" + idgoogle + idfire,Toast.LENGTH_SHORT).show();
                }

            });
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "iniciar sesion",Toast.LENGTH_SHORT).show();
        }


        return v;
    }




}