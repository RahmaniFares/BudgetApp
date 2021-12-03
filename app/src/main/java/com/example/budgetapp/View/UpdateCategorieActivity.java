package com.example.budgetapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.model.Categorie;
import com.example.budgetapp.model.Transaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateCategorieActivity extends AppCompatActivity {
    EditText nomCat,couleurCat,descrpCat;
    String typeCat;
    RadioGroup rdgrpTransCat;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button modifierrCategorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_categorie);
//intent
        Intent data = getIntent();
        final String selectedKey = data.getStringExtra("selectedKey");
        final String nomcat = data.getStringExtra("nomcat");
        String couleurcat = data.getStringExtra("couleurcat");
        String descpcat = data.getStringExtra("descpcat");
        String typecat = data.getStringExtra("typecat");
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Categories");

        //
        nomCat = findViewById(R.id.upcatnom); // nom cat
        rdgrpTransCat = findViewById(R.id.upcatrdgrp);// dépense ou reven
        couleurCat = findViewById(R.id.upcatcolor); // couleur
        descrpCat = findViewById(R.id.upcatdescp);// description

        nomCat.setText(nomcat);
        couleurCat.setText(couleurcat);
        descrpCat.setText(descpcat);
        if(typecat.equals("dépense"))
        {
            RadioButton rdep =findViewById(R.id.upcatrbdep);
            rdep.setChecked(true);
        }else if(typecat.equals("revenu")){
            RadioButton rrev=findViewById(R.id.upcatrevrd);
            rrev.setChecked(true);
        }

        modifierrCategorie = findViewById(R.id.btnvalidupdcat);

        rdgrpTransCat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.upcatrbdep:
                        Toast.makeText(getApplicationContext(),"depense",Toast.LENGTH_SHORT).show();
                        typeCat = "dépense";
                        break;
                    case R.id.upcatrevrd :
                        Toast.makeText(getApplicationContext(),"revenu",Toast.LENGTH_SHORT).show();
                        typeCat = "revenu";
                        break;
                }
            }
        });
    modifierrCategorie.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseReference
                    .child(selectedKey)
                    .setValue(new  Categorie(
                            nomCat.getText().toString(),
                            couleurCat.getText().toString(),
                            typeCat,
                            descrpCat.getText().toString()
                    ))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateCategorieActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateCategorieActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    });





    }
}