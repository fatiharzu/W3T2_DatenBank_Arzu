package com.example.w3t2_datenbank_arzu;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private EditText edTier, edBeine;
    private Button btnSpeichern, btnAnzeigenV1, btnAnzeigenV2;
    private TextView tvAnzeige;
    private Datenbankzeugs datenbankzeugs;

    // ==============================================================
    private class MyOCL implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (v == btnAnzeigenV1)
                tabelleninhaltAnzeigen(1);
            else if (v == btnAnzeigenV2)
                tabelleninhaltAnzeigen(2);
            else if (v == btnSpeichern)
                speichern();
        }
    }

    // ==============================================================
    private void speichern()
    {
        String t = edTier.getText().toString();
        int b = Integer.valueOf(edBeine.getText().toString());

        String sql = String.format("INSERT INTO Tiere (Tier, Beine) VALUES ('%s', %d)", t, b);
        datenbankzeugs.ausfuehren(sql);

        edTier.setText("");
        edBeine.setText("");
    }

    // --------------------------------------------------------------
    private void tabelleninhaltAnzeigen(int version)
    {
        ArrayList<ArrayList<Object>> inhalt = null;

        if (version == 1)
            inhalt = datenbankzeugs.auslesenV1();
        else if (version == 2)
            inhalt = datenbankzeugs.auslesenV2();

        tvAnzeige.setText("");

        for (ArrayList zeile : inhalt)
        {
            for (Object zelle : zeile)
                if (zelle instanceof String)
                    tvAnzeige.append(String.format("%-10s ", zelle.toString()));
                else
                    tvAnzeige.append(String.format("%3s ", zelle.toString()));

            tvAnzeige.append("\n");
        }
    }

    // --------------------------------------------------------------
    private void initComponents()
    {
        MyOCL ocl = new MyOCL();

        edTier = (EditText) findViewById(R.id.edTier);
        edBeine = (EditText) findViewById(R.id.edBeine);

        btnSpeichern = (Button) findViewById(R.id.btnSpeichern);
        btnSpeichern.setOnClickListener(ocl);
        btnAnzeigenV1 = (Button) findViewById(R.id.btnAnzeigenV1);
        btnAnzeigenV1.setOnClickListener(ocl);
        btnAnzeigenV2 = (Button) findViewById(R.id.btnAnzeigenV2);
        btnAnzeigenV2.setOnClickListener(ocl);

        tvAnzeige = (TextView) findViewById(R.id.tvAnzeige);
    }


    // --------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        datenbankzeugs = new Datenbankzeugs(this, "W3T2");
    }
}