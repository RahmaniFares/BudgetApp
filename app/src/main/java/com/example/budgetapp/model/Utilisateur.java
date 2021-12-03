package com.example.budgetapp.model;

public class Utilisateur {
    private String id;
    private String adresse;
    private String codepostal;
    private String devise;
    private String email;
    private String fName;
    private String phone;
    private float solde;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public Utilisateur(String adresse, String codepostal, String devise, String email,String fName,String phone,float solde) {
        this.adresse = adresse;
        this.codepostal = codepostal;
        this.devise = devise;
        this.email = email;
        this.fName = fName;
        this.phone = phone;
        this.solde = solde;
    }
}
