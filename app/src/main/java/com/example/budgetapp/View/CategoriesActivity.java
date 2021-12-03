package com.example.budgetapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.budgetapp.Adapter.CategoriesAdapter;
import com.example.budgetapp.R;
import com.example.budgetapp.ViewHolders.ViewHolderCategorie;
import com.example.budgetapp.model.Categorie;
import com.example.budgetapp.model.Transaction;
import com.example.budgetapp.util.ItemClickListener;
import com.example.budgetapp.util.ItemLongClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class CategoriesActivity extends AppCompatActivity {

    EditText searchcat;
    RecyclerView recyclerView;
    Button btnAjoutCat;
    private DatabaseReference postRef;
    private FirebaseRecyclerAdapter<Categorie, ViewHolderCategorie> adapter;
    private FirebaseRecyclerOptions<Categorie> options;

    private ArrayList<Categorie> categoriesArrayList;

    Categorie selectedCategorie;
    String selectedKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

       searchcat = findViewById(R.id.searchcat);
       recyclerView = findViewById(R.id.recyclercat);
       btnAjoutCat = findViewById(R.id.btnAjoutCat);
        postRef = FirebaseDatabase.getInstance().getReference().child("Categories");

        categoriesArrayList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        btnAjoutCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddCategorieActivity.class));
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        searchcat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    searchPosts(s.toString());
                } else {
                    fillRecyclerViewData();
                }
            }
        });


        fillRecyclerViewData();

    }

    private void searchPosts(String searchString) {
        Query query = postRef.orderByChild("nomCategorie")
                .startAt(searchString)
                .endAt(searchString + "/uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    categoriesArrayList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        final Categorie categorie = data.getValue(Categorie.class);
                        categorie.setId(data.getKey());
                        categoriesArrayList.add(categorie);
                    }

                    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getApplicationContext(), categoriesArrayList);
                    recyclerView.setAdapter(categoriesAdapter);
                    categoriesAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillRecyclerViewData() {

        options = new FirebaseRecyclerOptions.Builder<Categorie>()
                .setQuery(postRef, Categorie.class).build();

        adapter = new FirebaseRecyclerAdapter<Categorie, ViewHolderCategorie>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCategorie holder, int i, @NonNull Categorie categorie) {
                /*Picasso.get().load(model.getImage()).into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(CategoriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
                holder.nomCat.setText(categorie.getNomCategorie());
                holder.descripCat.setText(categorie.getDescription());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        selectedCategorie =categorie;
                        selectedKey = getSnapshots().getSnapshot(position).getKey();
                        Intent i = new Intent(view.getContext(),UpdateCategorieActivity.class);
                        i.putExtra("selectedKey",selectedKey);
                        i.putExtra("nomcat",categorie.getNomCategorie());
                        i.putExtra("couleurcat",categorie.getColorCategorie());
                        i.putExtra("descpcat",categorie.getDescription());
                        i.putExtra("typecat",categorie.getTypeCategorie());
                        startActivity(i);
                    }
                });

                holder.setItemLongClickListener(new ItemLongClickListener() {
                    @Override
                    public void onLongClick(View view, int position) {
                        AlertDialog alertDialog = new   AlertDialog.Builder(CategoriesActivity.this).setTitle("Suppression").setMessage("voulez-vous supprimer le produit ?").
                                setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        selectedCategorie = categorie;
                                        selectedKey = getSnapshots().getSnapshot(position).getKey();
                                        Log.d("Key Item", "" + selectedKey);
                                       postRef
                                                .child(selectedKey)
                                                .removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                        alertDialog.show();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderCategorie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorie_item,parent, false);
                return new ViewHolderCategorie(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}