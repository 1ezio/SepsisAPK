package com.sepsis.sepsis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sepsis.sepsis.model.records;
import com.sepsis.sepsis.model.recycler_model;

public class my_records extends AppCompatActivity {
    AutoCompleteTextView username;
    DatabaseReference databaseReference, reference;
    RecyclerView recyclerView;
    Button btn;
    FirebaseRecyclerAdapter adapter;
    String username2;
    String e=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_records);
        username= findViewById(R.id.editText6);
        btn=(Button)findViewById(R.id.button4);
        recyclerView= (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(getResources().getColor(R.color.blueAccent));
        }
        fetch();
        reference = FirebaseDatabase.getInstance().getReference("patient");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnap) {
                        if (dataSnap.exists()){
                            for (DataSnapshot shot: dataSnap.getChildren()){
                                String user= shot.child("email").getValue().toString();
                                if (username.getText().toString().equals(user)){
                                    final   dialog_of_report dialog= new dialog_of_report();
                                    Bundle args = new Bundle();
                                     String string =username.getText().toString();
                                    args.putString("num", string);
                                    dialog.setArguments(args);
                                    dialog.show(getSupportFragmentManager(), "");


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




        databaseReference  = FirebaseDatabase.getInstance().getReference("patient");
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for ( DataSnapshot  postsnapshot: dataSnapshot.getChildren()){
                          username2=postsnapshot.child("email").getValue().toString();
                          adapter.add(username2);

                        }



                    }
                username.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fetch(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");

        progressDialog.show();

        Query query = FirebaseDatabase.getInstance().getReference("patient");

        FirebaseRecyclerOptions<recycler_model> options=new FirebaseRecyclerOptions.Builder<recycler_model>().setQuery(query, new SnapshotParser<recycler_model>() {
            @NonNull
            @Override
            public recycler_model parseSnapshot(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();

                String n =null;
                String d=null;
                for (DataSnapshot snap:snapshot.getChildren()){
                 e=snapshot .child("email").getValue().toString();
                 n=snapshot.child("name").getValue().toString();
                 d=snapshot.child("doc_name").getValue().toString();
                }
                return new recycler_model(e,n,d);
            }
        }).build();



        adapter= new FirebaseRecyclerAdapter<recycler_model, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final recycler_model model) {
                holder.setname(model.getName());
                holder.setdoc(model.getDoc());
                holder.setusername(model.getUsername());



                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final   dialog_of_report2 dialog1= new dialog_of_report2();

                        Bundle  args= new Bundle();
                       final String string = e;
                        args.putString("num1",string);
                        dialog1.setArguments(args);
                        dialog1.show(getSupportFragmentManager(),"");
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list, viewGroup,false);
                return new ViewHolder(view);
            }





        };
        recyclerView.setAdapter(adapter);



    }

    public void save(View view) {


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView namet,doct, usernamet;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.list_root);
            namet=itemView.findViewById(R.id.n_id);
            doct=itemView.findViewById(R.id.d_id);
            usernamet=itemView.findViewById(R.id.user_id);

        }
        public void setname(String string){
            namet.setText("Name : "+string);
        }
        public void setdoc(String string){
            doct.setText("Doctor Name : "+string);

        }
        public void setusername(String string){
            usernamet.setText("Username : "+string);
        }
    }
    @Override
    protected void onStart() {
        adapter.startListening();

        super.onStart();
    }
    @Override
    protected void onStop() {
        adapter.stopListening();

        super.onStop();
    }
}
