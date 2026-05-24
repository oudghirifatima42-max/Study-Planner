package com.example.studyplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "studyplanner.db";
    private static final int    DB_VERSION = 2; // on passe à 2 pour créer la table chapitres
    private static final String TABLE      = "matiere";
    private static final String TABLE_CHAP = "chapitre";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    // Méthode appelée automatiquement à la création de la base
    public void onCreate(SQLiteDatabase db) {
        // Table matières (inchangée)
        String sql = "CREATE TABLE " + TABLE + " ("
                + "id            INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nom           TEXT, "
                + "coefficient   INTEGER, "
                + "heures        INTEGER, "
                + "date_examen   TEXT, "
                + "note_objectif REAL)";
        db.execSQL(sql);

        // Table chapitres
        String sqlChap = "CREATE TABLE " + TABLE_CHAP + " ("
                + "id          INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nom         TEXT, "
                + "matiere_id  INTEGER, "
                + "termine     INTEGER DEFAULT 0)"; // 0 = non terminé, 1 = terminé
        db.execSQL(sqlChap);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la base existait en version 1, on ajoute juste la table chapitres
        if (oldVersion < 2) {
            String sqlChap = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAP + " ("
                    + "id          INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nom         TEXT, "
                    + "matiere_id  INTEGER, "
                    + "termine     INTEGER DEFAULT 0)";
            db.execSQL(sqlChap);
        }
    }

    //  CRUD — MATIERES (inchangé)

    public boolean addMatiere(Matiere m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom",           m.getNom());
        values.put("coefficient",   m.getCoefficient());
        values.put("heures",        m.getHeures());
        values.put("date_examen",   m.getDateExamen());
        values.put("note_objectif", m.getNoteObjectif());
        long result = db.insert(TABLE, null, values);
        db.close();
        return result != -1;
    }

    public List<Matiere> getAllMatieres() {
        List<Matiere> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                Matiere m = new Matiere(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getFloat(5)
                );
                list.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public Matiere getMatiereById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE id=?",
                new String[]{String.valueOf(id)});
        Matiere m = null;
        if (cursor.moveToFirst()) {
            m = new Matiere(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getFloat(5)
            );
        }
        cursor.close();
        db.close();
        return m;
    }

    public boolean updateMatiere(Matiere m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom",           m.getNom());
        values.put("coefficient",   m.getCoefficient());
        values.put("heures",        m.getHeures());
        values.put("date_examen",   m.getDateExamen());
        values.put("note_objectif", m.getNoteObjectif());
        int rows = db.update(TABLE, values, "id=?",
                new String[]{String.valueOf(m.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteMatiere(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Supprimer aussi les chapitres liés à cette matière
        db.delete(TABLE_CHAP, "matiere_id=?", new String[]{String.valueOf(id)});
        int rows = db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public int countMatieres() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE, null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // ════════════════════════════════════════════════════════════
    //  CRUD — CHAPITRES (nouveau)
    // ════════════════════════════════════════════════════════════

    // CREATE — Ajouter un chapitre
    public boolean addChapitre(Chapitre c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom",        c.getNom());
        values.put("matiere_id", c.getMatiereId());
        values.put("termine",    0); // nouveau chapitre = non terminé
        long result = db.insert(TABLE_CHAP, null, values);
        db.close();
        return result != -1;
    }

    // READ — Récupérer tous les chapitres d'une matière
    public List<Chapitre> getChapitresByMatiere(int matiereId) {
        List<Chapitre> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_CHAP + " WHERE matiere_id=?",
                new String[]{String.valueOf(matiereId)});
        if (cursor.moveToFirst()) {
            do {
                Chapitre c = new Chapitre(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3)
                );
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // UPDATE — Marquer un chapitre comme terminé ou non
    public boolean updateTermine(int chapitreId, int termine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("termine", termine);
        int rows = db.update(TABLE_CHAP, values, "id=?",
                new String[]{String.valueOf(chapitreId)});
        db.close();
        return rows > 0;
    }

    // DELETE — Supprimer un chapitre
    public boolean deleteChapitre(int chapitreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CHAP, "id=?",
                new String[]{String.valueOf(chapitreId)});
        db.close();
        return rows > 0;
    }

    // Compter chapitres terminés (pour la progression)
    public int countChapitresTermines(int matiereId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_CHAP + " WHERE matiere_id=? AND termine=1",
                new String[]{String.valueOf(matiereId)});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Compter total chapitres d'une matière
    public int countChapitresTotal(int matiereId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_CHAP + " WHERE matiere_id=?",
                new String[]{String.valueOf(matiereId)});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}