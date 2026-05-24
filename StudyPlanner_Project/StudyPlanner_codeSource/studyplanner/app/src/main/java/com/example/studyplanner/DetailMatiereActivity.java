package com.example.studyplanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetailMatiereActivity extends AppCompatActivity {

    private DatabaseHelper db;   //Acces SQLite
    private int matiereId;  //ID de la matière sélectionnée

    // Affichent les informations de la matière.
    private TextView tvDetailNom, tvDetailCoef, tvDetailHeures, tvDetailDate, tvDetailNote;

    // Affichent la progression des chapitres terminés.
    private ProgressBar progressBar;
    private TextView tvProgression;

    // Chapitres
    private ListView listViewChapitres;   //Affiche tous les chapitres.
    private TextView tvAucunChapitre;    //Affiché si aucun chapitre n’existe(Message)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_matiere);

        db        = new DatabaseHelper(this);
        matiereId = getIntent().getIntExtra("id", -1);   //Récupère l’ID envoyé depuis l’activité précédente.

        if (matiereId == -1) { finish(); return; }    //Ferme l’activité si aucun ID n’est reçu.

        // Lier les vues
        tvDetailNom     = findViewById(R.id.tvDetailNom);
        tvDetailCoef    = findViewById(R.id.tvDetailCoef);
        tvDetailHeures  = findViewById(R.id.tvDetailHeures);
        tvDetailDate    = findViewById(R.id.tvDetailDate);
        tvDetailNote    = findViewById(R.id.tvDetailNote);
        progressBar     = findViewById(R.id.progressBar);
        tvProgression   = findViewById(R.id.tvProgression);
        listViewChapitres = findViewById(R.id.listViewChapitres);
        tvAucunChapitre = findViewById(R.id.tvAucunChapitre);

        Button btnModifier  = findViewById(R.id.btnModifier);
        Button btnSupprimer = findViewById(R.id.btnSupprimer);
        Button btnRetour    = findViewById(R.id.btnRetour);
        Button btnAjouterChapitre = findViewById(R.id.btnAjouterChapitre);

        // Bouton modifier matière
        btnModifier.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateMatiereActivity.class);
            intent.putExtra("id", matiereId);
            startActivity(intent);
        });

        // Bouton supprimer matière
        btnSupprimer.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Suppression")
                        .setMessage("Supprimer cette matiere et tous ses chapitres ?")
                        .setPositiveButton("Oui", (d, w) -> {
                            db.deleteMatiere(matiereId);
                            Toast.makeText(this, "Matiere supprimee", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .setNegativeButton("Non", null)
                        .show());

        btnRetour.setOnClickListener(v -> finish());

        // Bouton ajouter un chapitre
        btnAjouterChapitre.setOnClickListener(v -> afficherDialogAjouterChapitre());

        // Clic sur un chapitre → options (marquer terminé / supprimer)
        listViewChapitres.setOnItemClickListener((parent, view, position, id) -> {   //Action lorsqu’on clique sur un chapitre.
            List<Chapitre> chapitres = db.getChapitresByMatiere(matiereId);
            Chapitre chapitre = chapitres.get(position);
            afficherOptionsChapite(chapitre);   //Affiche :terminer/non terminé,     supprimer.

        });
    }

    @Override
    //Recharge les données après retour à l’activité.
    protected void onResume() {
        super.onResume();
        afficherDetails();
        afficherChapitres();
    }

    // Afficher les infos de la matière
    private void afficherDetails() {
        Matiere m = db.getMatiereById(matiereId);  //Récupère la matière depuis SQLite.
        if (m == null) { finish(); return; }

        //Affiche les informations dans les TextView.
        tvDetailNom.setText(m.getNom());
        tvDetailCoef.setText("Coefficient : " + m.getCoefficient());
        tvDetailHeures.setText("Heures prevues : " + m.getHeures() + "h");
        tvDetailDate.setText("Examen : " + m.getDateExamen());
        tvDetailNote.setText("Objectif : " + m.getNoteObjectif() + "/20");
    }

    //Afficher la liste des chapitres + progression
    private void afficherChapitres() {
        List<Chapitre> chapitres = db.getChapitresByMatiere(matiereId);   //Récupère tous les chapitres.
        int total    = chapitres.size(); //Calculer total des chapitres
        int termines = db.countChapitresTermines(matiereId);

        // Mise à jour de la barre de progression
        if (total == 0) {
            progressBar.setProgress(0);
            tvProgression.setText("Progression : 0% (aucun chapitre)");
        } else {
            int pourcent = (termines * 100) / total;
            progressBar.setProgress(pourcent);
            tvProgression.setText("Progression : " + termines + "/" + total + " chapitres termines (" + pourcent + "%)");
        }

        // Afficher la liste   setVisibility: (Affiche ou cache des vues.)
        if (chapitres.isEmpty()) {
            tvAucunChapitre.setVisibility(View.VISIBLE);
            listViewChapitres.setVisibility(View.GONE);
        } else {
            tvAucunChapitre.setVisibility(View.GONE);
            listViewChapitres.setVisibility(View.VISIBLE);

            // Construire les textes pour l'adapter
            String[] items = new String[chapitres.size()];  //Stocke les textes affichés dans la liste.
            for (int i = 0; i < chapitres.size(); i++) {
                Chapitre c = chapitres.get(i);
                items[i] = (c.isTermine() ? "✅ " : "⬜ ") + c.getNom();  //Emoji progression
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    items
            );
            listViewChapitres.setAdapter(adapter);
        }
    }

    //  Dialog : ajouter un chapitre
    private void afficherDialogAjouterChapitre() {
        EditText etNom = new EditText(this);
        etNom.setHint("Nom du chapitre");

        new AlertDialog.Builder(this)
                .setTitle("Ajouter un chapitre")
                .setView(etNom)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String nom = etNom.getText().toString().trim();
                    if (nom.isEmpty()) {
                        Toast.makeText(this, "Entrez un nom", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Chapitre c = new Chapitre();
                    c.setNom(nom);
                    c.setMatiereId(matiereId);
                    db.addChapitre(c);
                    afficherChapitres(); // rafraîchir
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    //  Dialog : options d'un chapitre (marquer / supprimer)
    private void afficherOptionsChapite(Chapitre chapitre) {
        String statut = chapitre.isTermine() ? "Marquer comme non termine" : "Marquer comme termine ✅";

        new AlertDialog.Builder(this)
                .setTitle(chapitre.getNom())
                .setItems(new String[]{statut, "Supprimer ce chapitre"}, (dialog, which) -> {
                    if (which == 0) {
                        // Basculer terminé / non terminé
                        int nouvelEtat = chapitre.isTermine() ? 0 : 1;
                        db.updateTermine(chapitre.getId(), nouvelEtat);
                        afficherChapitres(); // rafraîchir
                    } else {
                        // Supprimer
                        new AlertDialog.Builder(this)
                                .setTitle("Supprimer")
                                .setMessage("Supprimer le chapitre \"" + chapitre.getNom() + "\" ?")
                                .setPositiveButton("Oui", (d, w) -> {
                                    db.deleteChapitre(chapitre.getId());
                                    afficherChapitres();
                                })
                                .setNegativeButton("Non", null)
                                .show();
                    }
                })
                .show();
    }
}