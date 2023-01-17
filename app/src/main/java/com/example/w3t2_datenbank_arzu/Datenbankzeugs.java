package com.example.w3t2_datenbank_arzu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Datenbankzeugs extends SQLiteOpenHelper
{
    private Context context;

    // --------------------------------------------------------------
    // Pflichtmethoden
    // --------------------------------------------------------------

    public Datenbankzeugs(Context context, String dbName)
    {
        super(context, dbName, null, 1);
        this.context = context;
    }

    /**
     * Wird beim Erzeugen einer neuen Datenbank aufgerufen! Dies passiert aber erst nach dem
     * Einfügen des ersten Datensatzes.
     *
     * @param db die zu bearbeitende Datenbank
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE Tiere ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "Tier VARCHAR(255), "
                + "Beine INTEGER);";
        db.execSQL(sql);
    }

    /**
     * Wird aufgerufen, sobald die neue Versionsnummer anders ist
     * als die alte. Hier koennen dann Strukturanderungen der
     * Datenbank vorgenommen werden. Z.B. wenn eine weitere Tabelle
     * noetig ist oder eine alte Tabelle eine weitere
     * Spalte bekommen muss.
     * Werte duerfen sich hier nicht aendern (ein Hund mit
     * vier Beinen darf nicht
     * ploetzlich sechs Beine haben)!!!!!!!!!!!!!!!!!!!!!!!!
     * Ein Downgrade ist NICHT moeglich!!! Jede neue Versionsnummer
     * muss groesser sein als die alte. Es kommt ansonsten ein
     * android.database.sqlite.SQLiteException: Can't downgrade database from version 2 to 1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if ((oldVersion == 1) && (newVersion == 2))
        {
            Log.v("---> onUpgrade", "Upgrade von 1 auf 2");
            // CREATE
            // ALTER
        }
    }

    // --------------------------------------------------------------
    // Eigene Methoden
    // --------------------------------------------------------------

    public void ausfuehren(String sql)
    {
        // öffnet DB zum Schreiben
        SQLiteDatabase db = getWritableDatabase();
        // führt Anweisung aus
        db.execSQL(sql);
        // schließt Datenbankverbindung
        db.close();
    }

    // --------------------------------------------------------------

    /**
     * Auslesen der Tiere-Tabelle und Rückgabe in einer ArrayList.
     */
    public ArrayList<ArrayList<Object>> auslesenV1()
    {
        ArrayList<ArrayList<Object>> zeilen = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        // Anfrage an die Datenbank
        Cursor resultSet = db.rawQuery("SELECT ID, Tier, Beine FROM Tiere;", null);

        /*
         "moveToFirst" bewegt den Zeiger zum ersten Element. Ausserdem liefert es "true",
         falls was im Cursor enthalten ist und "false", falls nichts in der
         Tabelle stand, was zum Select-Statement passt.
         */
        if (resultSet.moveToFirst())
        {
            do
            {
                int id = resultSet.getInt(0);
                String t = resultSet.getString(1);
                int b = resultSet.getInt(2);

                ArrayList<Object> zeile = new ArrayList<>();
                zeile.add(id);
                zeile.add(t);
                zeile.add(b);
                zeilen.add(zeile);
            } while (resultSet.moveToNext());
        }

        db.close();

        return zeilen;
    }

    // --------------------------------------------------------------

    /**
     * Auslesen der Tiere-Tabelle und Rückgabe in einer ArrayList.
     */
    public ArrayList<ArrayList<Object>> auslesenV2()
    {
        ArrayList<ArrayList<Object>> zeilen = new ArrayList<>();
        ArrayList<Object> zeile;

        SQLiteDatabase db = getReadableDatabase();

        // Anfrage an die Datenbank
        Cursor resultSet = db.rawQuery("SELECT ID, Tier, Beine FROM Tiere;", null);

        /*
         "moveToFirst" bewegt den Zeiger zum ersten Element. Ausserdem liefert es "true",
         falls was im Cursor enthalten ist und "false", falls nichts in der
         Tabelle stand, was zum Select-Statement passt.
         */
        if (resultSet.moveToFirst())
        {
            int anzahlSpalten = resultSet.getColumnCount();
            String[] spaltennamen = resultSet.getColumnNames();

            zeile = new ArrayList<>();
            for (int i = 0; i < anzahlSpalten; i++)
                zeile.add(spaltennamen[i]);
            zeilen.add(zeile);

            do
            {
                zeile = new ArrayList<>();

                for (int spalte = 0; spalte < anzahlSpalten; spalte++)
                    zeile.add(resultSet.getString(spalte));

                zeilen.add(zeile);
            } while (resultSet.moveToNext());
        }

        db.close();

        return zeilen;
    }

    // --------------------------------------------------------------

}