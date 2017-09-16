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

public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer ;
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
    } ;

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
        words.add(new Word("lutti","one",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("oṭiiko","two",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("tolookosu","three",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("oyyiisa","four",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("tmassokka","five",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("temmokka","six",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("kenekaku","seven",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("kawinṭa","eight",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("wo'e","nine",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("na'aacha","ten",R.drawable.number_ten,R.raw.number_ten));

        WordAdapter itemsAdapter = new WordAdapter(this, words,R.color.category_numbers);

        final ListView listView = (ListView) findViewById(R.id.list);


        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ColorsActivity.this,"toast haha",Toast.LENGTH_LONG).show();
                Word temp_word= (Word)listView.getItemAtPosition(i);
                //request audio focus
                mAudioManger = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                int resultOfAudioFocus = mAudioManger.requestAudioFocus(listener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                releaseMediaPlayer();
                if (resultOfAudioFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, temp_word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }


            }
        });

 }
    /**
     * Clean up the media player by releasing its resources.
     */
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
