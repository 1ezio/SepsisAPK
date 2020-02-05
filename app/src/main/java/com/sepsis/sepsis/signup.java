package com.sepsis.sepsis;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepsis.sepsis.model.staff_regis_model;

public class signup extends AppCompatActivity {


    EditText name , password, username, empno,cnfpass,hospital_name;
    Button btn;
    int flag =0;
    DatabaseReference ref;
    SessionManager session;
    staff_regis_model model;
    ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkAccent));
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_font));
        }
        setContentView(R.layout.signup);

        name= (EditText)findViewById(R.id.editText_name);
        empno= (EditText)findViewById(R.id.editText_empno);
        username= (EditText)findViewById(R.id.editText_uname);
        password= (EditText)findViewById(R.id.editText_pass);
        cnfpass= (EditText)findViewById(R.id.editText_confPass);
        hospital_name=(EditText)findViewById(R.id.editText_empno2);

        btn=(Button)findViewById(R.id.signup_signup);
        session = new SessionManager(getApplicationContext());

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        model= new staff_regis_model();


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isInternetAvailable()){

                        if(name.getText().toString().isEmpty())
                        {
                            name.setError("Name can't be empty !");
                        }
                        if(empno.getText().toString().isEmpty()){
                            empno.setError("Employee Number can't be empty !");
                        }
                        if(username.getText().toString().isEmpty()){
                            username.setError("Username can't be empty !");
                        }
                        if(password.getText().toString().isEmpty()){
                            password.setError("Password can't be empty !");
                        }
                        if(cnfpass.getText().toString().isEmpty()){
                            cnfpass.setError("Confirm password can't be empty !");

                        }else{

                            String email = username.getText().toString().trim();
                            if(email.matches(emailPattern)){
                                if(cnfpass.getText().toString().equals(password.getText().toString())){
                                       if(password.getText().toString().length()>=8){
                                           progressBar.setVisibility(View.VISIBLE);
                                           staffdata();
                                           Handler handler = new Handler();
                                           handler.postDelayed(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Intent i = new Intent(signup.this, dashboard.class);
                                                   // i.putExtra("message", username.getText().toString());
                                                   startActivity(i);
                                                   session.createLoginSession("SIH", username.getText().toString());
                                                   finish();
                                               }
                                           }, 2000);
                                       }
                                       else{
                                           password.setError("Password must be at least 8 characters long !");
                                       }
                                    }
                                   else{
                                       if(cnfpass.getText().toString().isEmpty()){
                                           cnfpass.setError("Please enter password again !");
                                       }
                                       else if(!(cnfpass.getText().toString().equals(password.getText().toString()))){
                                           cnfpass.setError("Passwords do not match :(");
                                       }
                                   }
                               }
                               else{
                                   Toast.makeText(signup.this, "Username already exists !!", Toast.LENGTH_SHORT).show();
                               }
                        }
                   }else{
                       Toast.makeText(signup.this, "No Internet Connection :(", Toast.LENGTH_SHORT).show();
                   }
                }
            });

    }


    public void staffdata(){
        ref= FirebaseDatabase.getInstance().getReference("staff").child("hospital").push();
        model.setMname(name.getText().toString());
        model.setMempno(empno.getText().toString());
        model.setPassword(password.getText().toString());
        model.setUsername(username.getText().toString());
        model.setHos_name(hospital_name.getText().toString());

        ref.child("name").setValue(model.getMname());
        ref.child("empno").setValue(model.getMempno());
        ref.child("password").setValue(model.getPassword());
        ref.child("username").setValue(model.getUsername());
        ref.child("hospital_name").setValue(model.getHos_name());

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
