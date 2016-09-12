package aohan.zl.echo.echo;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audio;
    String song;
    AudioManager.OnAudioFocusChangeListener af;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Button btnPlayMusic= (Button) findViewById(R.id.btnPlayMusic);


        if (btnPlayMusic != null) {
          mediaPlayer =new MediaPlayer();
           song= Environment.getExternalStorageDirectory().getAbsolutePath()+"/lbj.mp3";
            try {
                mediaPlayer.setDataSource(song);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btnPlayMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (audio==null)
                        audio= (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

                    if (af==null) {
                        af = new AudioManager.OnAudioFocusChangeListener() {
                            @Override
                            public void onAudioFocusChange(int focusChange) {

                            }
                        };
                    }

                    int result=audio.requestAudioFocus(af,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);

                    if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                        //执行播放音乐操作
                        Log.e("testLog:",song);
                        try {
                            if (mediaPlayer.isPlaying()){
                               mediaPlayer.pause();
                                btnPlayMusic.setText("继续播放");
                            }else{
                                mediaPlayer.start();
                                btnPlayMusic.setText("暂停播放");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemRefresh:
                Toast.makeText(getApplicationContext(),"刷新",Toast.LENGTH_SHORT).show();
                break;
            case R.id.itemReset:
                Toast.makeText(getApplicationContext(),"重置",Toast.LENGTH_SHORT).show();
                break;
            case R.id.itemShare:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra("data","测试数据");
                startActivity(Intent.createChooser(intent,"选择"));
                break;
            case R.id.itemToSelectFileActivity:
                startActivity(new Intent(getApplicationContext(),FileSelectActivity.class));
                break;
        }
        return  true;
    }


    public void endMusic(View view){
        if (audio!=null){
            audio.abandonAudioFocus(af);
        }
    }
}
