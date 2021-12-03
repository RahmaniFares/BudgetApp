package com.example.budgetapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.budgetapp.R;
import com.example.budgetapp.util.ItemClickListener;
import com.example.budgetapp.util.ItemLongClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolderTransaction extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener{

    public TextView titreTrans,dateTrans,montantTrans;
    public ImageView img;
    ItemClickListener itemClickListener;
    ItemLongClickListener itemLongClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }
    public ViewHolderTransaction(@NonNull View itemView){
        super(itemView);
        titreTrans = itemView.findViewById(R.id.itemtstitre);
        dateTrans  = itemView.findViewById(R.id.itemtsdate);
        montantTrans = itemView.findViewById(R.id.itemtsmontant);
        img = itemView.findViewById(R.id.imgtransac);
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