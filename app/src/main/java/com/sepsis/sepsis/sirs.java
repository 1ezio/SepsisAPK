package com.sepsis.sepsis;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sepsis.sepsis.model.sirs_class;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class sirs extends AppCompatActivity {
    DatabaseReference database;
    EditText age, resp_rate, sysbp, hr, dia_bp, body_t;
    TextView map,textView;
    Button save;
    EditText username_p;
    DatabaseReference reference;
    private sirs_class sirsClass;
    private float map1;
    Interpreter interpreter;
    ProgressBar progressBar;


    Float[] mean={(float)63.016672, (float)18.500810,(float)119.280812, (float)78.02193,(float)84.578042,(float)57.581449,(float)98.615987};
    Float[] std= {(float)16.130546,(float)5.186035,(float)20.183439,(float)14.392553,(float)16.325395,(float)9.384823,(float)0.816691};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkAccent));
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_font));
        }

        setContentView(R.layout.activity_sirs);
        age=(EditText)findViewById((R.id.age_id));
        save=(Button)findViewById(R.id.button);
        resp_rate=(EditText)findViewById((R.id.resp_id));
        sysbp=(EditText)findViewById((R.id.sisbp_id));
        hr=(EditText)findViewById((R.id.hr_id));
        dia_bp=(EditText)findViewById((R.id.diabp_id));
        username_p =(EditText) findViewById(R.id.username_id);
        body_t=(EditText)findViewById((R.id.bt_id));
        textView=(TextView)findViewById(R.id.textView) ;






        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);




        try {
            interpreter=new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sirsClass=new sirs_class();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(age.getText().toString().isEmpty())
                {
                    age.setError("Age cannot be empty!");
                }
                if(resp_rate.getText().toString().isEmpty())
                {
                    resp_rate.setError("Respiration Rate can't be empty!");
                }
                if(sysbp.getText().toString().isEmpty())
                {
                    sysbp.setError("Sys BP can't be empty!");
                }
                if(hr.getText().toString().isEmpty())
                {
                    hr.setError("Heart Rate can't be empty");
                }
                if(body_t.getText().toString().isEmpty())
                {
                    body_t.setError("Body temperature can't be empty!");
                }
                if(dia_bp.getText().toString().isEmpty())
                {
                    dia_bp.setError("Dia BP can't be empty");
                }
                else{

                    if((Integer.parseInt(age.getText().toString())) < 0 || (Integer.parseInt(age.getText().toString())>150)){
                        age.setError("This doesn't seem to be a valid age :(");
                    }
                    if((Integer.parseInt(resp_rate.getText().toString())) < 8 || (Integer.parseInt(resp_rate.getText().toString())>32)){
                        resp_rate.setError("Not a valid respiration rate !");
                    }
                    if((Integer.parseInt(sysbp.getText().toString())) < 60 || (Integer.parseInt(sysbp.getText().toString())>300)){
                        sysbp.setError("Invalid value !!");
                    }
                    if((Integer.parseInt(dia_bp.getText().toString())) < 40 || (Integer.parseInt(dia_bp.getText().toString())>180)){
                        dia_bp.setError("Invalid Value !!");
                    }
                    if((Integer.parseInt(hr.getText().toString())) < 40 || ((Integer.parseInt(hr.getText().toString()) > (220 - Integer.parseInt(age.getText().toString())) )  )  ){
                        hr.setError("Invalid Heart Rate value !!");
                    }
                    if((Integer.parseInt(body_t.getText().toString())) < 86 || (Integer.parseInt(body_t.getText().toString())>120)){
                        body_t.setError("Invalid body temperature !!");
                    }
                    else{
                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                float[][] floats= new float[1][8];
                                savedata();
                                floats[0][0]=(Float.parseFloat(age.getText().toString())-mean[0]/std[0]);
                                floats[0][1]=(Float.parseFloat(resp_rate.getText().toString())-mean[1]/std[1]);
                                floats[0][2]=(Float.parseFloat(sysbp.getText().toString())-mean[2]/std[2]);
                                floats[0][3]=(Float.parseFloat(String.valueOf((2* Float.parseFloat(dia_bp.getText().toString())+ Float.parseFloat(sysbp.getText().toString())/3)-mean[3]/std[3])));
                                floats[0][4]=(Float.parseFloat(hr.getText().toString())-mean[4]/std[4]);
                                floats[0][5]=(Float.parseFloat(dia_bp.getText().toString())-mean[5]/std[5]);
                                floats[0][6]=(Float.parseFloat(body_t.getText().toString())-mean[6]/std[6]);
                                float res= doinference(floats);
                                if (res>0.050 ){
                                    progressBar.setVisibility(View.GONE);
                                    textView.setText("Patient MAY HAVE Sepsis");
                                    textView.setTextColor(Color.RED);
                                }else if (Float.parseFloat(resp_rate.getText().toString())>20 &&Float.parseFloat(sysbp.getText().toString())<90 && Float.parseFloat(hr.getText().toString())>90&& Float.parseFloat(resp_rate.getText().toString())>100 || Float.parseFloat(body_t.getText().toString())<96){
                                    progressBar.setVisibility(View.GONE);
                                    textView.setText("Patient MAY HAVE Sepsis");
                                    textView.setTextColor(Color.RED);

                                }else {
                                    textView.setText("Patient DOES NOT have Sepsis");
                                    textView.setTextColor(Color.rgb(4,122,25));
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, 1500);
                    }

                }

            }
        });

    }

    public void savedata(){
        reference=FirebaseDatabase.getInstance().getReference("patient");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot postSnapshot) {
                if(postSnapshot.exists()){
                    for (DataSnapshot childs: postSnapshot.getChildren()){

                        String username= childs.child("email").getValue().toString();
                        if (username_p.getText().toString().equals(username)){
                            Toast.makeText(sirs.this, "User Exist", Toast.LENGTH_SHORT).show();
                            String key= postSnapshot.getKey();




                            database=FirebaseDatabase.getInstance().getReference("patient").child(key);
                            sirsClass.setMage(Float.parseFloat(age.getText().toString()));
                            sirsClass.setMresprate(Float.parseFloat(resp_rate.getText().toString()));
                            sirsClass.setSisbp(Float.parseFloat(sysbp.getText().toString()));
                            sirsClass.setMhr(Float.parseFloat(hr.getText().toString()));
                            sirsClass.setMdiabp(Float.parseFloat(dia_bp.getText().toString()));
                            sirsClass.setMbody_temp(Float.parseFloat(body_t.getText().toString()));
                            map1=2* Float.parseFloat(dia_bp.getText().toString())+ Float.parseFloat(sysbp.getText().toString())/3;
                            //map.setText((int) map1);
                            sirsClass.setMmap(map1);
                            childs.getRef().child("age").setValue(sirsClass.getMage());
                            childs.getRef().child("Resp").setValue(sirsClass.getMresprate());
                            childs.getRef().child("SBP").setValue(sirsClass.getSisbp());
                            childs.getRef().child("HR").setValue(sirsClass.getMhr());
                            childs.getRef().child("DBP").setValue(sirsClass.getMdiabp());
                            childs.getRef().child("Temp").setValue(sirsClass.getMbody_temp());
                            childs.getRef().child("MAP").setValue(sirsClass.getMmap());


                            /*database.child("age").setValue(sirsClass.getMage());
                            database.child("resp_rate").setValue(sirsClass.getMresprate());
                            database.child("sys_bp").setValue(sirsClass.getSisbp());
                            database.child("hr").setValue(sirsClass.getMhr());
                            database.child("dia_bp").setValue(sirsClass.getMdiabp());
                            database.child("body_temp").setValue(sirsClass.getMbody_temp());
                            database.child("MAP").setValue(sirsClass.getMmap());
*/
                        }
                        else{
                            Toast.makeText(sirs.this, "User Not Found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











    }
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("sepsis.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);

    }
    public  float doinference(float[][] input ){
        float[][] output=new float[1][1];
        interpreter.run(input, output);
        return output[0][0];
    }
}
