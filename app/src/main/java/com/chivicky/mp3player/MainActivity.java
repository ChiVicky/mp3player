package com.chivicky.mp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView mp3List;
    private final String[] mp3name = {"我們不一樣", "是我太傻了", "說散就散", "體面"};
    private final int[] resId = {R.raw.mp3_0, R.raw.mp3_1, R.raw.mp3_2, R.raw.mp3_3};
    private final int[] jpgId={R.drawable.mp3_0,R.drawable.mp3_1,R.drawable.mp3_2,R.drawable.mp3_3};
    private static MediaPlayer mediaPlayer;
    private Button music_btn;
    private boolean isComplete;
    private boolean LoopOn = false;
    private boolean SoundOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();//元件對應
        adapter();//適配器
        mp3_List();//mp3清單

        music_btn.setOnClickListener(this);
    }

    private void findViews() {
        mp3List = findViewById(R.id.mp3_list);
        music_btn = findViewById(R.id.music_btn);
    }

    private void adapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
//                android.R.layout.simple_list_item_1,
                R.layout.mp3_list_item,
                mp3name
        );
        mp3List.setAdapter(adapter);
    }

    private void mp3_List() {
        mp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, mp3name[position], Toast.LENGTH_SHORT).show();
//                playMusic(position);
                Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("mp3Name",mp3name[position]);
                bundle.putInt("resId",resId[position]);
                bundle.putInt("jpgId",jpgId[position]);
                bundle.putBoolean("LoopOn",LoopOn);
                bundle.putBoolean("SoundOff",SoundOff);
                bundle.putBoolean("isComplete",isComplete);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    private void playMusic(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resId[index]);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        music_btn.setText(R.string.pause);
        isComplete = false;

        this.setTitle(getResources().getString(R.string.app_name) + " - " + mp3name[index]);//app文字上方顯示在目前正在撥放的音樂名稱
        CompleteMusic();//音樂完畢
    }

    private void CompleteMusic() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isComplete = true;
                Toast.makeText(MainActivity.this, R.string.complete, Toast.LENGTH_SHORT).show();
                music_btn.setText(R.string.play);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (mediaPlayer == null) {
            Toast.makeText(this, R.string.choice, Toast.LENGTH_SHORT).show();
            return;
        }//尚未選擇音樂時
        if (mediaPlayer.isPlaying()) {//當播放中時
            mediaPlayer.pause();//音樂暫停
            music_btn.setText(R.string.resume);//按鍵文字修改成"恢復播放"
//            Toast.makeText(MainActivity.this, R.string.pause, Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.start();//讓音樂繼續播放
            music_btn.setText(R.string.pause);//按鍵文字修改成"暫停播放"
//            Toast.makeText(MainActivity.this, R.string.resume, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.SoundOff:
                SoundOff =! SoundOff;
                if(mediaPlayer != null){
                    if(SoundOff){
                        mediaPlayer.setVolume(0f,0f);
                    }else{
                        mediaPlayer.setVolume(1.0f,1.0f);
                    }
                }
                if (SoundOff) {
                    item.setTitle(getResources().getString(R.string.SoundOn));
                   Toast.makeText(MainActivity.this, R.string.SoundOff, Toast.LENGTH_SHORT).show();
                } else {
                    item.setTitle(getResources().getString(R.string.SoundOff));
                    Toast.makeText(MainActivity.this, R.string.SoundOn, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.LoopOn:

                LoopOn =! LoopOn;

                if(mediaPlayer != null){
                    if(LoopOn){
                        mediaPlayer.setLooping(true);
                    }else{
                        mediaPlayer.setLooping(false);                    }
                }
                if (LoopOn) {
                    item.setTitle(getResources().getString(R.string.LoopOff));
                    Toast.makeText(MainActivity.this, R.string.LoopOn, Toast.LENGTH_SHORT).show();
                } else {
                    item.setTitle(getResources().getString(R.string.LoopOn));
                    Toast.makeText(MainActivity.this, R.string.LoopOff, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.About:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}