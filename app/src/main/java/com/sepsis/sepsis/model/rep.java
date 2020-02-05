package com.sepsis.sepsis.model;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepsis.sepsis.R;

public class rep extends AppCompatActivity {
    TextView  dia_bp, hr, map, resp_rate, sys_bp, body_temp, nam, doc;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_box_2);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        doc=(TextView)findViewById(R.id.doc1_id2) ;
        nam=(TextView) findViewById(R.id.name1_id2);
        dia_bp=(TextView) findViewById(R.id.dia_id2);
        sys_bp=(TextView) findViewById(R.id.sys1_id2);
        hr=(TextView) findViewById(R.id.hr1_id2);
        map=(TextView) findViewById(R.id.map1_id2);
        resp_rate=(TextView) findViewById(R.id.resp1_id2);
        body_temp=(TextView) findViewById(R.id.temp1_id2);

        ref= FirebaseDatabase.getInstance().getReference("patient");

        //   final String user1= b.getString("num1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                Bundle bundle = getIntent().getExtras();
                String message = bundle.getString("message");
                if (dataSnapshot.exists()){
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                        String fuser= postsnapshot.child("email").getValue().toString();
                        if (message.equals(fuser)){
                            String nam1=postsnapshot.child("name").getValue().toString();

                            nam.setText(nam1);
                            doc.setText(postsnapshot.child("doc_name").getValue().toString());
                            hr.setText(postsnapshot.child("HR").getValue().toString()+" BPM");
                            dia_bp.setText(postsnapshot.child("DBP").getValue().toString()+" mmHg");
                            map.setText(postsnapshot.child("MAP").getValue().toString()+ " mmHg");
                            resp_rate.setText(postsnapshot.child("Resp").getValue().toString()+ " breadth/min");
                            body_temp.setText(postsnapshot.child("Temp").getValue().toString()+ " °F" );
                            sys_bp.setText(postsnapshot.child("SBP").getValue().toString()+" mmHg");


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
