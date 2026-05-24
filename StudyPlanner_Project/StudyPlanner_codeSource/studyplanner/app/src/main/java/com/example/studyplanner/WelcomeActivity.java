package com.example.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        EditText etNom = findViewById(R.id.etNomEtudiant);
        Button   btnCommencer = findViewById(R.id.btnCommencer);

        btnCommencer.setOnClickListener(v -> {
            String nom = etNom.getText().toString().trim();
            if (nom.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer votre nom", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("nom", nom);
            startActivity(intent);
        });
    }
}