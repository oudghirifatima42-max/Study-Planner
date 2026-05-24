package com.example.studyplanner;

public class Matiere {

    private int id;
    private String nom;
    private int coefficient;
    private int heures;
    private String dateExamen;
    private float noteObjectif;

    public Matiere() {}

    public Matiere(int id, String nom, int coefficient, int heures, String dateExamen, float noteObjectif) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.heures = heures;
        this.dateExamen = dateExamen;
        this.noteObjectif = noteObjectif;
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

    public int getCoefficient() {
        return coefficient;
    }
    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getHeures() {
        return heures;
    }
    public void setHeures(int heures) {
        this.heures = heures;
    }

    public String getDateExamen() {
        return dateExamen;
    }
    public void setDateExamen(String dateExamen) {
        this.dateExamen = dateExamen;
    }

    public float getNoteObjectif() {
        return noteObjectif;
    }
    public void setNoteObjectif(float noteObjectif) {
        this.noteObjectif = noteObjectif;
    }
}