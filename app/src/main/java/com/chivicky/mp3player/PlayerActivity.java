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
    private ImageView title;
    private ImageButton play_btn;
    private ImageButton stop_btn;
    private ImageButton pause_btn;
    private String mp3Name;
    private int resId,jpgId;
    private boolean LoopOn;
    private boolean SoundOff;
    private boolean isComplete;
    private MediaPlayer mediaPlayer;

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
        title = findViewById(R.id.title);
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
        CompleteMusic();//音樂完畢
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
                //尚未選擇音樂時
                if (mediaPlayer.isPlaying()) {//當播放中時
                    mediaPlayer.pause();//音樂暫停
                    play_btn.setImageResource(R.drawable.music_play);//按鍵文字修改成"恢復播放"
                } else {
                    mediaPlayer.start();//讓音樂繼續播放
                    play_btn.setImageResource(R.drawable.music_pause);//按鍵文字修改成"暫停播放"
                }
                break;
            case R.id.stop_btn:
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                play_btn.setImageResource(R.drawable.music_play);//按鍵文字修改成"恢復播放"
                break;
        }
    }
}