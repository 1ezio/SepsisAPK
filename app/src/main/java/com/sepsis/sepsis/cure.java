package com.sepsis.sepsis;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


public class cure extends AppCompatActivity {

    VideoView videoView ;
    MediaController mediaController;
    Button play;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cure);
        videoView = findViewById(R.id.videoView);
        play = findViewById(R.id.play);

    }

    public void playVideo (View v)
    {
        mediaController = new MediaController(this);
        String path = "android.resource://com.sepsis.sepsis/" + R.raw.sepsis;
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);

        if (flag == 0) {

            videoView.start();
            play.setText("STOP VIDEO");
            flag =1;
        }
        else{
            videoView.stopPlayback();
            play.setText("PLAY VIDEO");
            flag = 0;
        }
    }

}
