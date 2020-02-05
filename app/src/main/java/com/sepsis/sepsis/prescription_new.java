package com.sepsis.sepsis;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class prescription_new extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkAccent));
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_font));
        }
        setContentView(R.layout.prescription_new);
    }
}
