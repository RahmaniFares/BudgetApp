package com.example.budgetapp.model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Transaction {

    @Exclude
    private  String id;
    private String titreTransaction;
    private float montant;
    private String categorie;
    private String typeTransaction;
    private String description;
    private Date dateTransaction;


    public Transaction(){

    }
    public Transaction( String titreTransaction, float montant, String categorie, String typeTransaction, String description, Date dateTransaction) {
        this.titreTransaction = titreTransaction;
        this.montant = montant;
        this.categorie = categorie;
        this.typeTransaction = typeTransaction;
        this.description = description;
        this.dateTransaction = dateTransaction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitreTransaction() {
        return titreTransaction;
    }

    public void setTitreTransaction(String titreTransaction) {
        this.titreTransaction = titreTransaction;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Date dateTransaction) {
        this.dateTransaction = dateTransaction;
    }
}
