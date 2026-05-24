package com.example.studyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMatiereActivity extends AppCompatActivity {

    private EditText etNom, etCoef, etHeures, etDate, etNote;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matiere);

        db       = new DatabaseHelper(this);
        etNom    = findViewById(R.id.etNom);
        etCoef   = findViewById(R.id.etCoef);
        etHeures = findViewById(R.id.etHeures);
        etDate   = findViewById(R.id.etDate);
        etNote   = findViewById(R.id.etNote);

        Button btnSave   = findViewById(R.id.btnSave);
        Button btnAnnuler = findViewById(R.id.btnAnnuler);

        btnSave.setOnClickListener(v -> enregistrer());
        btnAnnuler.setOnClickListener(v -> finish());
    }

    private void enregistrer() {
        String nom  = etNom.getText().toString().trim();
        String coef = etCoef.getText().toString().trim();
        String h    = etHeures.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        if (nom.isEmpty() || coef.isEmpty() || h.isEmpty() || date.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Matiere m = new Matiere(
                0,
                nom,
                Integer.parseInt(coef),
                Integer.parseInt(h),
                date,
                Float.parseFloat(note)
        );

        boolean ok = db.addMatiere(m);
        if (ok) {
            Toast.makeText(this, "Matiere ajoutee !", Toast.LENGTH_SHORT).show();
            finish(); // retour a la liste
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
        }
    }
}