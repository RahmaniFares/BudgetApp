package com.example.budgetapp.View.fragment_MainActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgetapp.View.UpdateTransactionActivity;
import com.example.budgetapp.ViewHolders.ViewHolderTransaction;
import com.example.budgetapp.model.Transaction;
import com.example.budgetapp.R;
import com.example.budgetapp.util.ItemClickListener;
import com.example.budgetapp.util.ItemLongClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HistoricFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Transaction> options;
    FirebaseRecyclerAdapter<Transaction, ViewHolderTransaction> adapter;
    Transaction selectedTransaction;
    String selectedKey;

    public HistoricFragment() {
    }

    public static HistoricFragment newInstance(String param1, String param2) {
        HistoricFragment fragment = new HistoricFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_historic, container, false);
        recyclerView = view.findViewById(R.id.recycletrans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = (DatabaseReference) firebaseDatabase.getReference("Transactions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    displayComment();


        return view;
    }
    private void displayComment() {
        options = new FirebaseRecyclerOptions.Builder<Transaction>()
                .setQuery(databaseReference, Transaction.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Transaction, ViewHolderTransaction>(options) {


            @NonNull
            @Override
            public ViewHolderTransaction onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.transaction_item,parent,false);

                return new ViewHolderTransaction(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderTransaction holder, int i, @NonNull Transaction transaction) {
                holder.titreTrans.setText(transaction.getTitreTransaction());
                holder.dateTrans.setText(transaction.getDateTransaction().toString());
                if(transaction.getTypeTransaction().equals("dépense")){
                    holder.montantTrans.setText("-"+"$ "+transaction.getMontant()+" DT");
                    holder.montantTrans.setTextColor(getResources().getColor(R.color.dépense));
                    holder.img.setImageDrawable(getResources().getDrawable(R.drawable.revenu));
                }else {
                    holder.montantTrans.setText("+" + "$ " + transaction.getMontant() + " DT");
                holder.montantTrans.setTextColor(getResources().getColor(R.color.revenu));

                }
                holder.setItemLongClickListener(new ItemLongClickListener() {
                    @Override
                    public void onLongClick(View view, int position) {


                        AlertDialog alertDialog = new   AlertDialog.Builder(getActivity()).setTitle("Suppression").setMessage("voulez-vous supprimer le produit ?").
                                setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        selectedTransaction = transaction;
                                        selectedKey = getSnapshots().getSnapshot(position).getKey();
                                        Log.d("Key Item", "" + selectedKey);
                                        databaseReference
                                                .child(selectedKey)
                                                .removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
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
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        selectedTransaction = transaction;
                        selectedKey = getSnapshots().getSnapshot(position).getKey();
                        Intent i = new Intent(view.getContext(), UpdateTransactionActivity.class);
                        i.putExtra("selectedKey",selectedKey);
                        i.putExtra("titretrans",transaction.getTitreTransaction());
                        i.putExtra("montanttrans",transaction.getMontant()+"");
                        i.putExtra("typetrans",transaction.getTypeTransaction());
                        i.putExtra("cattrans",transaction.getCategorie());
                        i.putExtra("datetrans",transaction.getDateTransaction()+"");
                        i.putExtra("desctrans",transaction.getDescription());
                        startActivity(i);

                    }
                });
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}