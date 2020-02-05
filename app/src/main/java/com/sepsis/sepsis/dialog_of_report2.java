package com.sepsis.sepsis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dialog_of_report2 extends DialogFragment {
    TextView ok, dia_bp, hr, map, resp_rate, sys_bp, body_temp, nam, doc;
    DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.dialog_box_2,container, false);
        ok = view.findViewById(R.id.textView212);

        doc=(TextView)view.findViewById(R.id.doc1_id2) ;
        nam=(TextView) view.findViewById(R.id.name1_id2);
        dia_bp=(TextView) view.findViewById(R.id.dia_id2);
        sys_bp=(TextView) view.findViewById(R.id.sys1_id2);
        hr=(TextView) view.findViewById(R.id.hr1_id2);
        map=(TextView) view.findViewById(R.id.map1_id2);
        resp_rate=(TextView) view.findViewById(R.id.resp1_id2);
        body_temp=(TextView) view.findViewById(R.id.temp1_id2);

        ref= FirebaseDatabase.getInstance().getReference("patient");
        Bundle b= getArguments();
        final String user= b.getString("num1");
     //   final String user1= b.getString("num1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()){

                        if (user.equals(postsnapshot.child("email").getValue().toString())){
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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });






        return view;
    }
}
