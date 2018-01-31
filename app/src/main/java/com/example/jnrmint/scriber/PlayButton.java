package com.example.jnrmint.scriber;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by tim-b on 31/01/2018.
 */

public class PlayButton extends android.support.v7.widget.AppCompatImageButton {

    private boolean mStartRecording;
    private boolean mStartPlaying;
    private boolean mFirstPlay;
    private MediaPlayer mediaPlayer;
    private Timer time;
    private TextView status;
    private PlayButton mPlayIcon = null;

    OnClickListener clickEvent = new OnClickListener() {


        @Override
        public void onClick(View view) {
            if(mStartRecording) {
                onPlay(mStartPlaying);
                while(mPlayer.isPlaying()) {
                    if (mStartPlaying) {
                        if (mFirstPlay) {
                            mPlayIcon.setBackgroundResource(R.drawable.ic_pse_mb);
                            //timeView.setText("00:00");
                            resetTime();
                            status.setText("First Play");
                            time.startTimer();
                            mFirstPlay = !mFirstPlay;
                        } else {
                            status.setText("Second Play");
                            this.setBackgroundResource(R.drawable.ic_pse_mb);
                            time.startTimer();
                        }
                    } else {
                        mPlayIcon.setBackgroundResource(R.drawable.ic_ply_mb);
                        time.stopTimer();
                    }/*}  else    {
                        mPlayIcon.setBackgroundResource(R.drawable.ic_ply_mb);
                        //timeView.setText("00:00");
                        time.stopTimer();
                        resetTime();
                        status.setText("Playing finished");
                    }*/
                    mStartPlaying = !mStartPlaying;
                }
            } else {
                status.setText("Cannot play whilst recording!");
            }
        }
    };

    public PlayButton(Context context) {
        super(context);
        this.setOnClickListener(clickEvent);
    }

    public void getAttribute(Boolean sP, Boolean sR, Boolean fp, )

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
