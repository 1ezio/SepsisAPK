package com.sepsis.sepsis;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class dashboard extends AppCompatActivity {

    private LinearLayout prescriptionLayout,sirs_layout,patient_layout, my_records;
    ImageView prescription,sirs,quick_checkup,cure,patient;
    TextView Usenname, hopital_name;
    ImageView logout;
    DatabaseReference databaseReference;
    SessionManager session;

    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_main);
        hopital_name=(TextView) findViewById(R.id.textView20);
        session = new SessionManager(getApplicationContext());


        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);
        session.checkLogin();
        final String hosp=email;
        databaseReference= FirebaseDatabase.getInstance().getReference("staff").child("hospital");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("username").getValue().toString().equals(hosp)){
                            hopital_name.setText(snapshot.child("hospital_name").getValue().toString());
                        }



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        Usenname= findViewById(R.id.username);
        //Bundle bundle = getIntent().getExtras();
        //String message = bundle.getString("message");
        Usenname.setText(email);//changed for now
        prescriptionLayout = findViewById(R.id.layout_prescription);
        sirs_layout = findViewById(R.id.layout_sirs);
        patient_layout = findViewById(R.id.layout_patient);
        my_records= findViewById(R.id.layout_my_patients);

        prescription = findViewById(R.id.medicines);
        sirs = findViewById(R.id.image_sirs);
        patient = findViewById(R.id.image_patient);
        logout = findViewById(R.id.user);


        prescriptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prescription_layout = new Intent(dashboard.this, prescription.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(dashboard.this,prescription,"prescriptionTransition");
                startActivity(prescription_layout, options.toBundle());
            }
        });
        sirs_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, sirs.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(dashboard.this,sirs,"sirsTransition");
                startActivity(i,options.toBundle());
            }
        });

        patient_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, patient_registration.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(dashboard.this,patient,"patientTransition");
                startActivity(i,options.toBundle());
            }
        });

        my_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this, my_records.class);
               // ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(dashboard.this,patient,"");
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(dashboard.this); //Home is name of the activity
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        session.logoutUser();
                        finish();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }



    public void sofa(View v)
    {
        Intent activity = new Intent(dashboard.this, my_records.class);
        startActivity(activity);
    }

    public void cure(View v)
    {
        Intent activity = new Intent(dashboard.this, cure.class);
        startActivity(activity);
    }

    public void quickCheckup(View v)
    {
        Intent activity = new Intent(dashboard.this, HeartRate.class);
        startActivity(activity);
    }

}
