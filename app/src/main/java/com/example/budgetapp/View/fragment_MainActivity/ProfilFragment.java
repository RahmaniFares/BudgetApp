package com.example.budgetapp.View.fragment_MainActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.budgetapp.R;
import com.example.budgetapp.View.UpdateProfilActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;


public class ProfilFragment extends Fragment {
    Button btnUpProfile;
    TextView nomProf,adresseProf,telProf,dateProf,codepostProf,mailProf;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;

    public ProfilFragment() {
        // Required empty public constructor
    }

    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        // Inflate the layout for this fragment
        btnUpProfile = v.findViewById(R.id.btnUpdateProfile);
        nomProf = v.findViewById(R.id.profilename);
        mailProf = v.findViewById(R.id.mailprofile);
        dateProf = v.findViewById(R.id.datenaissprofile);
        telProf = v.findViewById(R.id.telprofile);
        adresseProf = v.findViewById(R.id.adrprofile);
        codepostProf = v.findViewById(R.id.codepprofile);
        profileImage =v.findViewById(R.id.imgProfile);
//
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
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {


                if (snapshot != null && snapshot.exists()) {
                    nomProf.setText(snapshot.getString("fName"));
                    mailProf.setText(snapshot.getString("email"));
                    adresseProf.setText(snapshot.getString("adresse"));
                    telProf.setText(snapshot.getString("phone"));
                    codepostProf.setText(snapshot.getString("codepostal"));
                } else {
                 }
            }
        });


        //
        btnUpProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),UpdateProfilActivity.class));
            }
        });
        return v;
    }
}