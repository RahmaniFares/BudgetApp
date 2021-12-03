package com.example.budgetapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.model.Categorie;
import com.example.budgetapp.model.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText titreTrans,dateTrans,descripTrans,montantTrans;
    String categorieTrans,typeTrans;
    Spinner catspin;
    RadioGroup rdgrpTransType;
    private DatabaseReference catData;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Button ajouterTrans = findViewById(R.id.btnAddTrans);
        rdgrpTransType = findViewById(R.id.addrdgrptrans);
        titreTrans = findViewById(R.id.addtitreTrans);
        dateTrans= findViewById(R.id.addeditDateTrans);
        descripTrans = findViewById(R.id.adddescripTrans);
        montantTrans = findViewById( R.id.addmontant);
        catspin = findViewById( R.id.addspinnercat);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Transactions");
        catData= FirebaseDatabase.getInstance().getReference().child("Categories");
        rdgrpTransType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.addrddeptrans:
                        Toast.makeText(getApplicationContext(),"depense",Toast.LENGTH_SHORT).show();
                        typeTrans = "dépense";
                    break;
                    case R.id.addrdrevtrans:
                        Toast.makeText(getApplicationContext(),"revenu",Toast.LENGTH_SHORT).show();
                        typeTrans = "revenu";
                        break;
                }
            }
        });

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
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspin.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspin.setAdapter(arrayAdapter);
        catspin.setOnItemSelectedListener(this);


        ajouterTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titreTransaction  = titreTrans.getText().toString();
                float montant = Float.parseFloat(montantTrans.getText().toString());
                String categorie =categorieTrans;
                String typeTransaction =typeTrans ;
                String description = descripTrans.getText().toString();
                Date dateTransaction = Calendar.getInstance().getTime();
              /*
                if(TextUtils.isEmpty(nomProduit)){
                    nomProd.setError("Veuillez saisir le nom de produit");
                    return;
                }
                if(TextUtils.isEmpty(quantiteProduit)){
                    quantiteProd.setError("Quantité vide");
                    return;
                }
                if(TextUtils.isEmpty(quantiteMinProduit)){
                    quantiteMinProd.setError("Quantité minimal vide");
                    return;
                }
*/
                Transaction transaction = new Transaction(
                        titreTransaction,
                        montant,
                        categorie,
                        typeTransaction,
                        description,
                        Calendar.getInstance().getTime());
                databaseReference.push()
                        .setValue(transaction);
                Toast.makeText(AddTransactionActivity.this,"Transaction effectué",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()== R.id.addspinnercat) {
            categorieTrans = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(this, categorieTrans, Toast.LENGTH_SHORT).show();
        }}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}