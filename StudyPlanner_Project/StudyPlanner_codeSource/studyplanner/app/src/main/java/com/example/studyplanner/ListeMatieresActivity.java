package com.example.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListeMatieresActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper db;
    private MatiereAdapter adapter;
    private List<Matiere> matieres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_matieres);

        db       = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);

        //Action lors du clic sur le FAB.pour ajouter une nouvelle matière.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddMatiereActivity.class)));
    }

    @Override
    //Exécutée quand on revient à cette activité.Exemple :après ajout,modification,suppression.
    protected void onResume() {
        super.onResume();
        // Recharger la liste a chaque retour (apres add / update / delete)
        matieres = db.getAllMatieres();
        adapter  = new MatiereAdapter(this, matieres);
        listView.setAdapter(adapter);
    }
}