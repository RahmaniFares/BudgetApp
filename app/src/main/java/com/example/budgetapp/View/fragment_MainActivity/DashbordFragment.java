package com.example.budgetapp.View.fragment_MainActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.budgetapp.View.AddCategorieActivity;
import com.example.budgetapp.View.AddTransactionActivity;
import com.example.budgetapp.R;
import com.example.budgetapp.View.CategoriesActivity;
import com.example.budgetapp.View.RappelActivity;
import com.example.budgetapp.model.Categorie;
import com.example.budgetapp.model.Transaction;
import com.example.budgetapp.model.Utilisateur;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class DashbordFragment extends Fragment {
    private PieChart chart;
    private Typeface tf;
    private TextView montantDash,revenuDash,dépenseDash;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    private Utilisateur utilisateur;
    ImageView profileImage;
    StorageReference storageReference;
    private DatabaseReference transData;

    public DashbordFragment() {
        // Required empty public constructor
    }
 public static DashbordFragment newInstance() {
         return new DashbordFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_dashbord, container, false);
        ImageView btnAjoutDepAct = v.findViewById(R.id.btnAjoutdepAct);
        ImageView btnAjoutRevAct = v.findViewById(R.id.btnAjoutrevAct);
        ImageView btnListCatAct = v.findViewById(R.id.btnListCat);
        ImageView btnAjoutRapActivity = v.findViewById(R.id.btnAjoutRapp);
        profileImage = v.findViewById(R.id.dashimgprof);
        chart = v.findViewById(R.id.pieChart1);
        montantDash = v.findViewById(R.id.dashmontant);
        revenuDash = v.findViewById(R.id.dashrevenu);
        dépenseDash = v.findViewById(R.id.dashdepense);
        transData= FirebaseDatabase.getInstance().getReference().child("Transactions");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/fares6.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });


        ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        Query query = transData;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    transactionArrayList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        final Transaction transaction = data.getValue(Transaction.class);
                        transaction.setId(data.getKey());
                        transactionArrayList.add(transaction);
                    }
                    dépenseDash.setText(calculDépense(transactionArrayList)+" DT");
                    revenuDash.setText(calculRevenu(transactionArrayList)+ " DT");
                    DocumentReference documentReference = fStore.collection("users").document(userId);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (snapshot != null && snapshot.exists()) {
                                utilisateur = new Utilisateur(
                                        snapshot.getString("adresse"),
                                        snapshot.getString("codepostal"),
                                        snapshot.getString("devise"),
                                        snapshot.getString("email"),
                                        snapshot.getString("fName"),
                                        snapshot.getString("phone"),
                                        snapshot.getLong("solde"));
                                montantDash.setText(calculMontantActuelle(utilisateur.getSolde(),transactionArrayList));

                            } else {
                                Toast.  makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //montantDash.setText(calculMontantActuelle(utilisateur.getSolde(),transactionArrayList));
        btnAjoutRapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), RappelActivity.class));
            }
        });
        btnAjoutDepAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddTransactionActivity.class));
            }
        });
        btnListCatAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), CategoriesActivity.class));
            }
        });


        initgraph();
        return v;
    }
    public void initgraph(){
        chart.getDescription().setEnabled(false);


        chart.setCenterTextTypeface(tf);
        chart.setCenterText(generateCenterText());
        chart.setCenterTextSize(10f);
        chart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        chart.setHoleRadius(50f);
        chart.setTransparentCircleRadius(40f);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        chart.setData(generatePieData());

    }
    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Category");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }
    protected PieData generatePieData() {

        int count = 4;

        ArrayList<PieEntry> entries1 = new ArrayList<>();


            entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "réparation"));
        entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Nourriture"));
        entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Eléctricité"));
        entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Internet"));
        entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Salle de sport"));

        PieDataSet ds1 = new PieDataSet(entries1, "Categories");
        ds1.setColors(ColorTemplate.PASTEL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
       // d.setValueTypeface(tf);

        return d;
    }

    public String calculMontantActuelle(float montant,ArrayList<Transaction> listTrans){

        float res = montant;
        for (Transaction data : listTrans)
            if(data.getTypeTransaction().equals("dépense"))
                res  = res - data.getMontant();
            else
                res = res + data.getMontant();
        return res+"";
    };

    public String calculDépense(ArrayList<Transaction> listTrans){

        float res =0;
        for (Transaction data : listTrans)
            if(data.getTypeTransaction().equals("dépense"))
                res  +=  data.getMontant();

        return res+"";
    }
    public String calculRevenu(ArrayList<Transaction> listTrans){

        float res =0;
        for (Transaction data : listTrans)
            if(data.getTypeTransaction().equals("revenu"))
                res += data.getMontant();
        return res+"";
    }
}