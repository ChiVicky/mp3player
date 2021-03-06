package com.chivicky.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title_text;
    private ImageView title_img;
    private ImageButton play_btn;
    private ImageButton stop_btn;
    private ImageButton pause_btn;
    private String mp3Name;
    private int resId,jpgId;
    private boolean LoopOn;
    private boolean SoundOff;
    private boolean isComplete;
    private static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        findViews();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mp3Name = bundle.getString("mp3Name");
            resId = bundle.getInt("resId");
            LoopOn = bundle.getBoolean("LoopOn");
            SoundOff = bundle.getBoolean("SoundOff");
            isComplete = bundle.getBoolean("isComplete");
            jpgId=bundle.getInt("jpgId");
            title_text.setText(mp3Name);
            title_img.setImageResource(jpgId);
            play_btn.setEnabled(false);
            stop_btn.setEnabled(false);
            playdelay();
        }
        play_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);

    }

    private void playdelay() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    Thread.sleep(600);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                playMusic();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        play_btn.setEnabled(true);
                        stop_btn.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void findViews() {
        title_text = findViewById(R.id.title_text);
        title_img = findViewById(R.id.title_img);
        play_btn = findViewById(R.id.play_btn);
        stop_btn = findViewById(R.id.stop_btn);

    }

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
        play_btn.setImageResource(R.drawable.music_pause);
        isComplete = false;
        CompleteMusic();//????????????
    }

    private void CompleteMusic() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isComplete = true;
                play_btn.setImageResource(R.drawable.music_play);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_btn:
                if (mediaPlayer == null) {
                    playMusic();
                    return;
                }
                //?????????????????????
                if (mediaPlayer.isPlaying()) {//???????????????
                    mediaPlayer.pause();//????????????
                    play_btn.setImageResource(R.drawable.music_play);//?????????????????????"????????????"
                } else {
                    mediaPlayer.start();//?????????????????????
                    play_btn.setImageResource(R.drawable.music_pause);//?????????????????????"????????????"
                }
                break;
            case R.id.stop_btn:
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                play_btn.setImageResource(R.drawable.music_play);//?????????????????????"????????????"
                break;
        }
    }
}