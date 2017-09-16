package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
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
        words.add(new Word("әpә","father",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("әṭa","mother",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("angsi","son",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("tune","daughter",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("taachi","older brother",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("chalitti","younger brother",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("teṭe","older sister",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("kolliti","younger sister",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("ama","grandmother",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("paapa","grandfather",R.drawable.family_grandfather,R.raw.family_grandfather));
        WordAdapter itemsAdapter = new WordAdapter(this, words,R.color.category_family);

        listView = (ListView) findViewById(R.id.list);
        //use ListView and Adapter to reuse the views as much as possible to minimize the memory usage
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word temp_word= (Word)listView.getItemAtPosition(i);
                //clear existing media player memory before creating newones
                releaseMediaPlayer();
                //request audio focus from the android system
                mAudioManger = (AudioManager)getSystemService(AUDIO_SERVICE);
                int result = mAudioManger.requestAudioFocus(listener,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                //if audio focus is gained, create media player
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, temp_word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });

    }
    //can be called when 1. lost audio focus 2.activity move to stage of onStop() 3. finish current playing 4. before create new media player clean currently exisiting ones
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        // force to release the media player resource even though it has not finished playing current music
        //because the client has moved on
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
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
