package com.example.budgetapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.model.Categorie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategorieActivity extends AppCompatActivity {


    EditText nomCat,couleurCat,descrpCat;
    String typeCat;
    RadioGroup rdgrpTransCat;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button ajouterCategorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categorie);
        ajouterCategorie = findViewById(R.id.btnvalidajoutcat);

        nomCat = findViewById(R.id.catnom); // nom cat
        rdgrpTransCat = findViewById(R.id.catrdgrp);// dépense ou reven
        couleurCat = findViewById(R.id.catcolor); // couleur
        descrpCat = findViewById(R.id.catdescp);// description

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Categories");

        rdgrpTransCat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.catrbdep:
                        Toast.makeText(getApplicationContext(),"depense",Toast.LENGTH_SHORT).show();
                        typeCat = "dépense";
                        break;
                    case R.id.catrevrd :
                        Toast.makeText(getApplicationContext(),"revenu",Toast.LENGTH_SHORT).show();
                        typeCat = "revenu";
                        break;
                }
            }
        });


        ajouterCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categorie categorie = new Categorie(
                        nomCat.getText().toString(),
                        couleurCat.getText().toString(),
                        typeCat,
                        descrpCat.getText().toString()
                );
                databaseReference.push()
                        .setValue(categorie);
                Toast.makeText(AddCategorieActivity.this,"Categories Ajoutée",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}