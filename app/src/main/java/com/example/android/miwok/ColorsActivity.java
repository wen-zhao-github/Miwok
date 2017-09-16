package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    private ArrayList<Word> words;
    private ListView listView;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManger;
    private AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener(){

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            }

        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list); //share the word_list xml layout between four activities

        words = new ArrayList<Word>();
        words.add(new Word("weṭeṭṭi","red",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("chokokki","green",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("ṭakaakki","brown",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("ṭopoppi","gray",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("kululli","black",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("kelelli","white",R.drawable.color_white,R.raw.color_white));
        words.add(new Word("ṭopiisә","dusty yellow",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("chiwiiṭә","mustard yellow",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        WordAdapter itemsAdapter = new WordAdapter(this, words,R.color.category_colors);

        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //clear currently existing  media player  before create new ones
                releaseMediaPlayer();
                Word temp_word= (Word)listView.getItemAtPosition(i);
                mAudioManger = (AudioManager)getSystemService(AUDIO_SERVICE);
                int result = mAudioManger.requestAudioFocus(listener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, temp_word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }

            }
        });
    }
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManger.abandonAudioFocus(listener);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
