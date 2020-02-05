package com.sepsis.sepsis;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText  username, password;
    DatabaseReference reference;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkAccent));
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_font));
        }

        username= findViewById(R.id.editText_uname);
        password=  findViewById(R.id.editText_pass);
        button= findViewById(R.id.login_login);

        reference= FirebaseDatabase.getInstance().getReference("staff").child("details");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String fusername= snapshot.child("username").getValue().toString();
                                String fpassword=snapshot.child("password").getValue().toString();

                                if (username.equals(fusername)){
                                    if (password.equals(fpassword)){

                                        Toast.makeText(login.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(login.this, "Incorrect username", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
