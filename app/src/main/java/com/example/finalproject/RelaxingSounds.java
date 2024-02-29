package com.example.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class RelaxingSounds extends AppCompatActivity {

    private MediaPlayer[] mediaPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxing_sounds);

        Toolbar toolbar = findViewById(R.id.toolsbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Relaxing Sounds");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mediaPlayers = new MediaPlayer[]{
                MediaPlayer.create(this, R.raw.autumn_1),
                MediaPlayer.create(this, R.raw.forest_2),
                MediaPlayer.create(this, R.raw.ocean_6),
                MediaPlayer.create(this, R.raw.piano_4),
                MediaPlayer.create(this, R.raw.violin_5_cut),
                MediaPlayer.create(this, R.raw.rain_3)
        };

        ImageView[] imageViews = new ImageView[6];

        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);
        imageViews[4] = findViewById(R.id.imageView5);
        imageViews[5] = findViewById(R.id.imageView6);

        for (int i = 0; i < 6; i++) {
            final int finalI = i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(finalI);
                }
            });
        }
    }

    private void playSound(int index) {
        if (index >= 0 && index < mediaPlayers.length) {
            if (mediaPlayers[index].isPlaying()) {
                mediaPlayers[index].seekTo(0);
            } else {
                for (MediaPlayer mediaPlayer : mediaPlayers) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
                mediaPlayers[index].start();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.release();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
