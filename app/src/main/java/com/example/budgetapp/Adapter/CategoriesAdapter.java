package com.example.budgetapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.View.CategoriesActivity;
import com.example.budgetapp.View.UpdateCategorieActivity;
import com.example.budgetapp.ViewHolders.ViewHolderCategorie;
import com.example.budgetapp.model.Categorie;
import com.example.budgetapp.util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CategoriesAdapter extends RecyclerView.Adapter<ViewHolderCategorie> {

    public Context context;
    public ArrayList<Categorie> categoriesArrayList;

    public CategoriesAdapter(Context context, ArrayList<Categorie> categoriesArrayList) {
        this.context = context;
        this.categoriesArrayList = categoriesArrayList;
    }

    @NonNull
    @Override
    public ViewHolderCategorie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorie_item, parent, false);

        return new ViewHolderCategorie(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategorie holder, int position) {
        Categorie categorie = categoriesArrayList.get(position);
        holder.nomCat.setText(categorie.getNomCategorie());
        holder.descripCat.setText(categorie.getDescription());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,categorie.getId(),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(view.getContext(), UpdateCategorieActivity.class);
                        i.putExtra("selectedKey",categorie.getId());
                        i.putExtra("nomcat",categorie.getNomCategorie());
                        i.putExtra("couleurcat",categorie.getColorCategorie());
                        i.putExtra("descpcat",categorie.getDescription());
                        i.putExtra("typecat",categorie.getTypeCategorie());
                        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
