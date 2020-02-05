package com.sepsis.sepsis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class staff_login extends AppCompatActivity {
    EditText  username, password;
    DatabaseReference reference;
    Button button;
    ProgressBar progressBar;
    SessionManager session;


    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkAccent));
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_font));
        }
        setContentView(R.layout.login);

        username= (EditText) findViewById(R.id.editText_uname);
        password= (EditText) findViewById(R.id.editText_pass);
        button= (Button)findViewById(R.id.login_login);

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        session = new SessionManager(getApplicationContext());

        reference= FirebaseDatabase.getInstance().getReference("staff").child("hospital");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isInternetAvailable()){
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String fusername= snapshot.child("username").getValue().toString();
                                    String fpassword=snapshot.child("password").getValue().toString();

                                    if (username.getText().toString().equals(fusername)){
                                        if (password.getText().toString().equals(fpassword)){
                                            progressBar.setVisibility(View.VISIBLE);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent i = new Intent(staff_login.this, dashboard.class);
                                                    //i.putExtra("message", username.getText().toString());
                                                    startActivity(i);
                                                    session.createLoginSession("SIH", username.getText().toString());
                                                    finish();
                                                }
                                            }, 2000);

                                        }else{
                                            Toast.makeText(staff_login.this, "Incorrect password or Username", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(staff_login.this, "No Internet Connection :( ", Toast.LENGTH_SHORT).show();
                }


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

    public void signup(View view)
    {
        Intent i = new Intent(staff_login.this, signup.class);
        startActivity(i);

    }

    public boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

