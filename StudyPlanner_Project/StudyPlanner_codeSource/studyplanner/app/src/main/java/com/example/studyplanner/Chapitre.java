package com.example.studyplanner;

public class Chapitre {

    private int id;
    private String nom;
    private int matiereId;
    private int termine; // 0 = non terminé, 1 = terminé

    public Chapitre() {}

    public Chapitre(int id, String nom, int matiereId, int termine) {
        this.id = id;
        this.nom = nom;
        this.matiereId = matiereId;
        this.termine = termine;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getMatiereId() {
        return matiereId;
    }
    public void setMatiereId(int matiereId) {
        this.matiereId = matiereId;
    }

    public int getTermine() {
        return termine;
    }
    public void setTermine(int termine) {
        this.termine = termine;
    }

    public boolean isTermine() {
        return termine == 1;
    }
}