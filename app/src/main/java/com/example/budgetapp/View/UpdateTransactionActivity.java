package com.example.budgetapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetapp.Adapter.CategoriesAdapter;
import com.example.budgetapp.R;
import com.example.budgetapp.model.Categorie;
import com.example.budgetapp.model.Transaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.example.budgetapp.R.*;

public class UpdateTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText titreTrans,dateTrans,descripTrans,montantTrans;
    String categorieTrans,typeTrans;
    Spinner catspin;
    RadioGroup rdgrpTransType;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private DatabaseReference catData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_update_transaction);
        Button modifierTrans = findViewById(id.btnModifTrans);
        rdgrpTransType = findViewById(id.uprdgrptrans);
        titreTrans = findViewById(id.uptitreTrans);
        dateTrans= findViewById(id.upeditDateTrans);
        descripTrans = findViewById(id.updescripTrans);
        montantTrans = findViewById( id.upmontant);
        catspin = findViewById( id.upspinnercat);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Transactions");
        catData= FirebaseDatabase.getInstance().getReference().child("Categories");

     /*   String values[]={"jardinage","fsdf","fsfsd","sfsdf","sfsdf"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(values));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, layout.support_simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspin.setAdapter(arrayAdapter);
        catspin.setOnItemSelectedListener(this);*/
        ArrayList<Categorie> categoriesArrayList = new ArrayList<>();
        ArrayList<String> nomCategoriesArrayList = new ArrayList<>();
        Query query = catData;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    categoriesArrayList.clear();
                    nomCategoriesArrayList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        final Categorie categorie = data.getValue(Categorie.class);
                        categorie.setId(data.getKey());
                        categoriesArrayList.add(categorie);
                        nomCategoriesArrayList.add(categorie.getNomCategorie());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayList<String> arrayList = nomCategoriesArrayList;
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), layout.support_simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspin.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspin.setAdapter(arrayAdapter);
        catspin.setOnItemSelectedListener(this);


// intent
        Intent data = getIntent();
        final String selectedKey = data.getStringExtra("selectedKey");
        final String titretrans = data.getStringExtra("titretrans");
        String datetrans = data.getStringExtra("datetrans");
        String descptrans = data.getStringExtra("desctrans");
        String typetrans = data.getStringExtra("typetrans");
        String cattrans = data.getStringExtra("cattrans");
        String montanttrans = data.getStringExtra("montanttrans");

        // mettte des données actuels
        catspin.setSelection(arrayAdapter.getPosition(cattrans));
        titreTrans.setText(titretrans);
        descripTrans.setText(descptrans);
        dateTrans.setText(datetrans);
        montantTrans.setText(montanttrans);

        if(typetrans.equals("dépense")){
            RadioButton rbdep = findViewById(id.uprddeptrans);
            rbdep.setChecked(true);
        }else if (typetrans.equals("revenu")){
            RadioButton rbrev = findViewById(id.updrevtrans);
            rbrev.setChecked(true);
        }


        //firebase


        //
        rdgrpTransType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case id.uprddeptrans:
                        Toast.makeText(getApplicationContext(),"depense",Toast.LENGTH_SHORT).show();
                        typeTrans = "dépense";
                        break;
                    case id.updrevtrans :
                        Toast.makeText(getApplicationContext(),"revenu",Toast.LENGTH_SHORT).show();
                        typeTrans = "revenu";
                        break;
                }
            }
        });

        modifierTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference
                        .child(selectedKey)
                        .setValue(new Transaction(
                                titreTrans.getText().toString(),
                                Float.parseFloat(montantTrans.getText().toString()),
                                categorieTrans,
                                "dépense",
                                descripTrans.getText().toString(),
                                Calendar.getInstance().getTime()

                        ))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdateTransactionActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateTransactionActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()== id.upspinnercat) {
            categorieTrans = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(this, categorieTrans, Toast.LENGTH_SHORT).show();
        }}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public ArrayList<String> getListCat(Context context){


        return null;
    }

}