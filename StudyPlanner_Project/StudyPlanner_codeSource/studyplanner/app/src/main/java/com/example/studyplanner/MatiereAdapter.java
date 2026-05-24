package com.example.studyplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MatiereAdapter extends ArrayAdapter<Matiere> {

    private Context context;    //Ressources
    private List<Matiere> list;   //Liste des matieres
    private DatabaseHelper db;  //Acces a la BDD

    //Constructeur
    public MatiereAdapter(Context context, List<Matiere> list) {
        super(context, 0, list);
        this.context = context;
        this.list    = list;
        this.db      = new DatabaseHelper(context);  // Initialisation SQLite
    }

    //cette bloc kyt3awdd dans touts les exo f Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_matiere, parent, false);
        }

        // Récupère la matière actuelle
        Matiere m = list.get(position);

        //Recuperer les composants
        TextView tvNom   = convertView.findViewById(R.id.tvNom);
        TextView tvInfos = convertView.findViewById(R.id.tvInfos);
        ImageButton btnDetail  = convertView.findViewById(R.id.btnDetail);
        ImageButton btnEdit    = convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete  = convertView.findViewById(R.id.btnDelete);

        //Remplir les donnes (Nom.Infos,BtnDetail,btnEdit et btnDelete)
        tvNom.setText(m.getNom());
        tvInfos.setText("Coef " + m.getCoefficient() + "  •  " + m.getHeures() + "h  •  " + m.getDateExamen());

        // DETAIL
        btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailMatiereActivity.class);  // Ouvrir DetailMatiereActivity
            intent.putExtra("id", m.getId());  // Envoyer l'id de la matière
            context.startActivity(intent);    // Démarrer l'activité
        });

        // EDIT
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateMatiereActivity.class);
            intent.putExtra("id", m.getId());
            context.startActivity(intent);
        });

        //  DELETE
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)   // Fenêtre de confirmation
                    .setTitle("Suppression")     // Titre
                    .setMessage("Supprimer \"" + m.getNom() + "\" ?")     // Message
                    .setPositiveButton("Oui", (dialog, which) -> {   // Bouton Oui
                        db.deleteMatiere(m.getId());  // Supprimer SQLite
                        list.remove(position);   // Supprimer de la liste
                        notifyDataSetChanged();    // Actualiser la ListView
                        Toast.makeText(context, "Matiere supprimee", Toast.LENGTH_SHORT).show();    // Message succès
                    })
                    .setNegativeButton("Non", null)   // Bouton Non
                    .show();
        });

        // Retourne la vue finale
        return convertView;
    }
}