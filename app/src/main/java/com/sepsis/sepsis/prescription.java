package com.sepsis.sepsis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class prescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription);
    }

    public void onClickPrescription(View v){
        Intent i = new Intent(prescription.this,prescription_new.class);
        startActivity(i);
    }

}

