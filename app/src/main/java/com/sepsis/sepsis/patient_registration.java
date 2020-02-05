package com.sepsis.sepsis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepsis.sepsis.model.patientReg_model;


public class patient_registration extends AppCompatActivity {

    EditText name, username, age;
    AutoCompleteTextView doc_name;
    Button button;

    DatabaseReference databaseReference;
    patientReg_model patient_model;
    ProgressBar progressBar;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkAccent));
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_font));
        }
        setContentView(R.layout.activity_patient_registration);
        name= (EditText)findViewById(R.id.editText_name);
        age=(EditText)findViewById(R.id.editText_age);
        username=(EditText)findViewById(R.id.editText_username);
        doc_name=findViewById(R.id.editText_name2);
        button=(Button)findViewById(R.id.register);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        reference = FirebaseDatabase.getInstance().getReference("staff").child("hospital");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String  doc=snapshot.child("name").getValue().toString();
                        adapter.add(doc);
                    }


                }
                doc_name.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        patient_model=new patientReg_model();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        databaseReference= FirebaseDatabase.getInstance().getReference("patient").push();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        patient_model.setMname(name.getText().toString());

                        patient_model.setMage(age.getText().toString());
                        patient_model.setMusermae(username.getText().toString());
                        patient_model.setMdoc_name(doc_name.getText().toString());
                        databaseReference.child("doc_name").setValue(patient_model.getMdoc_name());
                        databaseReference.child("name").setValue(patient_model.getMname());
                        //databaseReference.child("empno").setValue(patientReg_model.get);
                        databaseReference.child("age").setValue(patient_model.getMage());
                        databaseReference.child("email").setValue(patient_model.getMusermae());
                        Toast.makeText(patient_registration.this, "Successfully Registered :)", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 2000);

            }
        });

    }
}
