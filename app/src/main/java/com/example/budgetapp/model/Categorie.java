package com.example.budgetapp.model;

import com.google.firebase.firestore.Exclude;

public class Categorie {

    @Exclude
    private String id;
    private String nomCategorie;
    private String colorCategorie;
    private String typeCategorie;
    private String description;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getColorCategorie() {
        return colorCategorie;
    }

    public void setColorCategorie(String colorCategorie) {
        this.colorCategorie = colorCategorie;
    }

    public String getTypeCategorie() {
        return typeCategorie;
    }

    public void setTypeCategorie(String typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    public Categorie(String nomCategorie, String colorCategorie, String typeCategorie,String description) {
        this.nomCategorie = nomCategorie;
        this.colorCategorie = colorCategorie;
        this.typeCategorie = typeCategorie;
        this.description = description;
    }
    public Categorie() {
    }
}


