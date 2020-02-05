package com.sepsis.sepsis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sepsis.sepsis.model.staff_regis_model;

public class staff_regis extends AppCompatActivity {
    EditText name , password, username, empno,cnfpass;
    Button btn;
    DatabaseReference ref;
    staff_regis_model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_regis);
        name= (EditText)findViewById(R.id.editText);
        empno= (EditText)findViewById(R.id.editText2);
        username= (EditText)findViewById(R.id.editText3);
        password= (EditText)findViewById(R.id.editText4);
        cnfpass= (EditText)findViewById(R.id.editText5);
        btn=(Button)findViewById(R.id.button2);

        model= new staff_regis_model();

        if (cnfpass.getText().toString().equals(password.getText().toString())){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    staffdata();;
                    Intent intent = new Intent(staff_regis.this,sirs.class);
                    startActivity(intent);
                }
            });
        }
    }
    public void staffdata(){
        ref= FirebaseDatabase.getInstance().getReference("staff").child("details").push();
        model.setMname(name.getText().toString());
        model.setMempno(empno.getText().toString());
        model.setPassword(password.getText().toString());
        model.setUsername(username.getText().toString());

        ref.child("name").setValue(model.getMname());
        ref.child("empno").setValue(model.getMempno());
        ref.child("password").setValue(model.getPassword());
        ref.child("username").setValue(model.getUsername());


    }
}
