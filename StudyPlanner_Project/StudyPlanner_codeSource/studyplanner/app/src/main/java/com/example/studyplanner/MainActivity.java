package com.example.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyplanner.R;
import com.example.studyplanner.DatabaseHelper;
import com.example.studyplanner.Matiere;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvNbMatieres, tvTotalHeures, tvNbExamens, tvBonjour;
    private TextView tvMat1, tvDate1, tvInfos1;
    private TextView tvMat2, tvDate2, tvInfos2;
    private TextView tvMat3, tvDate3, tvInfos3;
    private String nomEtudiant = "Étudiant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // Récupérer le nom transmis par WelcomeActivity via Intent
        nomEtudiant = getIntent().getStringExtra("nom");
        if (nomEtudiant == null || nomEtudiant.isEmpty())
            nomEtudiant = "Étudiant";

        tvBonjour     = findViewById(R.id.tvBonjour);
        tvNbMatieres  = findViewById(R.id.tvNbMatieres);
        tvTotalHeures = findViewById(R.id.tvTotalHeures);
        tvNbExamens   = findViewById(R.id.tvNbExamens);

        tvMat1   = findViewById(R.id.tvMat1);
        tvDate1  = findViewById(R.id.tvDate1);
        tvInfos1 = findViewById(R.id.tvInfos1);

        tvMat2   = findViewById(R.id.tvMat2);
        tvDate2  = findViewById(R.id.tvDate2);
        tvInfos2 = findViewById(R.id.tvInfos2);

        tvMat3   = findViewById(R.id.tvMat3);
        tvDate3  = findViewById(R.id.tvDate3);
        tvInfos3 = findViewById(R.id.tvInfos3);

        // Afficher le nom dès onCreate
        tvBonjour.setText("Bonjour, " + nomEtudiant + " 👋");

        Button btnVoirMatieres = findViewById(R.id.btnVoirMatieres);
        btnVoirMatieres.setOnClickListener(v ->
                startActivity(new Intent(this, ListeMatieresActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerDashboard();
    }

    private void chargerDashboard() {

        List<Matiere> liste = db.getAllMatieres();// Récupérer toutes les matières depuis SQLite
        int totalHeures = 0;  // Variable pour calculer le total des heures
        for (Matiere m : liste)   // Parcourir toutes les matières
            totalHeures += m.getHeures();   // Ajouter les heures de chaque matière


        tvNbMatieres.setText(String.valueOf(liste.size()));   // Afficher le nombre total des matières
        tvTotalHeures.setText(totalHeures + "h");   // Afficher le total des heures
        tvNbExamens.setText(String.valueOf(liste.size()));   // Afficher le nombre des examens

        remplirCard(liste, 0, tvMat1, tvDate1, tvInfos1);   // Remplir la première card matière
        remplirCard(liste, 1, tvMat2, tvDate2, tvInfos2); // Remplir la deuxième card matière
        remplirCard(liste, 2, tvMat3, tvDate3, tvInfos3);   // Remplir la troisième card matière
    }


    // Méthode pour remplir une card avec les informations d'une matière
    private void remplirCard(List<Matiere> liste, int index,
                             TextView tvNom,
                             TextView tvDate,
                             TextView tvInfos) {

        // Vérifier si une matière existe à cette position
        if (index < liste.size()) {
            Matiere m = liste.get(index); // Récupérer la matière selon l'index
            tvNom.setText(m.getNom());   // Afficher le nom de la matière
            tvDate.setText(m.getDateExamen());    // Afficher la date de l'examen
            // Afficher les informations supplémentaires
            tvInfos.setText(
                    "Coef " + m.getCoefficient()       // coefficient
                            + "  •  "
                            + m.getHeures() + "h"      // nombre d'heures
                            + "  •  Objectif : "
                            + m.getNoteObjectif()      // note objectif
                            + "/20"
            );

        } else {

            // Si aucune matière n'existe → afficher vide
            tvNom.setText("—");
            // Vider la date
            tvDate.setText("");
            // Vider les informations
            tvInfos.setText("");
        }
    }
}