package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManger;
    private AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener(){

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback because your Audio Focus was
                // temporarily stolen, but will be back soon.
                // i.e. for a phone call
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Stop playback, because you lost the Audio Focus.
                // i.e. the user started some other playback app
                // Remember to unregister your controls/buttons here.
                // And release the kra — Audio Focus!
                // You’re done.
                releaseMediaPlayer();

            } else if (focusChange ==
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Lower the volume, because something else is also
                // playing audio over you.
                // i.e. for notifications or navigation directions
                // Depending on your audio playback, you may prefer to
                // pause playback here instead. You do you.
                // mMediaPlayer.setVolume(0.4f,0.4f);
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback, because you hold the Audio Focus
                // again!
                // i.e. the phone call ended or the nav directions
                // are finished
                // If you implement ducking and lower the volume, be
                // sure to return it to normal here, as well.
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
        setContentView(R.layout.word_list);
        // setTitle("Numbers");
    /*LinearLayout rootView = (LinearLayout)  findViewById(R.id.rootView);
        for(int i =0; i <englishNumbers.size(); i++){
            TextView temp = new TextView(this);
            temp.setText(englishNumbers.get(i));
            rootView.addView(temp);
        }*/
        ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("minto wuksus","Where are you going?",R.raw.phrase_where_are_you_going));
        words.add(new Word("tinnә oyaase'nә","What is your name?",R.raw.phrase_what_is_your_name));
        words.add(new Word("oyaaset...","My name is...",R.raw.phrase_my_name_is));
        words.add(new Word("michәksәs?","How are you feeling?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("kuchi achit","I’m feeling good.",R.raw.phrase_im_feeling_good));
        words.add(new Word("әәnәs'aa?","Are you coming?",R.raw.phrase_are_you_coming));
        words.add(new Word("hәә’ әәnәm", "Yes, I’m coming.",R.raw.phrase_yes_im_coming));
        words.add(new Word("әәnәm","I’m coming.", R.raw.phrase_im_coming));
        words.add(new Word("yoowutis","Let’s go.",R.raw.phrase_lets_go));
        words.add(new Word("әnni'nem","Come here.",R.raw.phrase_come_here));
        WordAdapter itemsAdapter = new WordAdapter(this, words,R.color.category_phrases);

        final ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ColorsActivity.this,"toast haha",Toast.LENGTH_LONG).show();
                Word temp_word= (Word)listView.getItemAtPosition(i);
                releaseMediaPlayer();
                mAudioManger = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                int result_requestAudioFocus = mAudioManger.requestAudioFocus(listener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result_requestAudioFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, temp_word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }


            }
        });

    }
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.stop();
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
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
