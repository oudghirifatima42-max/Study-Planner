package com.example.studyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class UpdateMatiereActivity extends AppCompatActivity {

    private EditText etNom, etCoef, etHeures, etDate, etNote;
    private DatabaseHelper db;
    private int matiereId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_matiere);

        db        = new DatabaseHelper(this);
        matiereId = getIntent().getIntExtra("id", -1);

        if (matiereId == -1) { finish(); return; }

        etNom    = findViewById(R.id.etNom);
        etCoef   = findViewById(R.id.etCoef);
        etHeures = findViewById(R.id.etHeures);
        etDate   = findViewById(R.id.etDate);
        etNote   = findViewById(R.id.etNote);

        // Pre-remplir les champs avec les donnees existantes
        Matiere m = db.getMatiereById(matiereId);  //recupurer cette matiere a travers l'ID
        if (m != null) {
            //on doit recupurer les chamos
            etNom.setText(m.getNom());
            etCoef.setText(String.valueOf(m.getCoefficient()));
            etHeures.setText(String.valueOf(m.getHeures()));
            etDate.setText(m.getDateExamen());
            etNote.setText(String.valueOf(m.getNoteObjectif()));
        }

        Button btnUpdate  = findViewById(R.id.btnUpdate);
        Button btnAnnuler = findViewById(R.id.btnAnnuler);

        btnUpdate.setOnClickListener(v -> mettreAJour());
        btnAnnuler.setOnClickListener(v -> finish());
    }

    private void mettreAJour() {
        //Récupère les nouvelles valeurs saisies.
        String nom  = etNom.getText().toString().trim();
        String coef = etCoef.getText().toString().trim();
        String h    = etHeures.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        if (nom.isEmpty() || coef.isEmpty() || h.isEmpty() || date.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        //Crée une matière contenant les nouvelles données.
        Matiere m = new Matiere(
                matiereId,
                nom,
                Integer.parseInt(coef),  //Conversions  Text->entier
                Integer.parseInt(h),    ///Conversions  Text->entier
                date,
                Float.parseFloat(note)   ///Conversions  Text->Float
        );

        boolean ok = db.updateMatiere(m);
        if (ok) {
            Toast.makeText(this, "Matiere modifiee !", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
        }
    }
}