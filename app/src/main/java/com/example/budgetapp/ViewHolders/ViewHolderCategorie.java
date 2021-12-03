package com.example.budgetapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.example.budgetapp.R;
import com.example.budgetapp.util.ItemClickListener;
import com.example.budgetapp.util.ItemLongClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderCategorie extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener{

    public TextView nomCat,descripCat;
    ItemClickListener itemClickListener;
    ItemLongClickListener itemLongClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }
    public ViewHolderCategorie(@NonNull View itemView){
        super(itemView);
        nomCat = itemView.findViewById(R.id.itemnomcat);
        descripCat  = itemView.findViewById(R.id.itemdesccat);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        itemLongClickListener.onLongClick(view,getAdapterPosition());
        return true;
    }
}