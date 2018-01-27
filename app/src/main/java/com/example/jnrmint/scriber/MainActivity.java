package com.example.jnrmint.scriber;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView timeView = null;
    private TextView reset = null;
    private TextView status = null;
    private Timer time = null;

    private boolean mStartRecording = true;
    private boolean mStartPlaying = true;
    private boolean mFirstPlay = true;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private Button mRecordButton = null;
    private ImageButton mRecIcon = null;
    private MediaRecorder mRecorder = null;

    private Button   mPlayButton = null;
    private ImageButton mPlayIcon = null;
    private MediaPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onCreate(Bundle bun) {
        super.onCreate(bun);
        setContentView(R.layout.activity_main);


        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.AAC";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //Timer initiation
        timeView = findViewById(R.id.timer);
        reset = timeView;
        time = new Timer();
        time.setClock(timeView);

        status = findViewById(R.id.output);

        mRecordButton = findViewById(R.id.btnRec);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        onRecord(mStartRecording);
                        if (mStartRecording) {
                            mRecordButton.setText("STOP");
                            time.startTimer();
                        } else {
                            mRecordButton.setText("REC");
                            time.stopTimer();
                        }
                        mStartRecording = !mStartRecording;
                    }
                });

        mRecIcon = findViewById(R.id.imageButton3);
        mRecIcon.setBackgroundResource(R.drawable.ic_strt_rec);
        mRecIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    mRecIcon.setBackgroundResource(R.drawable.ic_stp_rec);
                    time.startTimer();
                } else {
                    mRecIcon.setBackgroundResource(R.drawable.ic_strt_rec);
                    time.stopTimer();
                }
                mStartRecording = !mStartRecording;
            }
        });

        mPlayButton = findViewById(R.id.btnPlay);
        mPlayButton.setText("PLAY");
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    //mPlayButton.setText("Stop");
                    time.startTimer();
                } else {
                    mPlayButton.setText("PLAY");
                    time.stopTimer();
                }
                mStartPlaying = !mStartPlaying;
            }
        });

        mPlayIcon = findViewById(R.id.imageButton);
        mPlayIcon.setBackgroundResource(R.drawable.ic_ply_mb);
        mPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    if(mFirstPlay){
                    mPlayIcon.setBackgroundResource(R.drawable.ic_pse_mb);
                    //timeView.setText("00:00");
                    timeView.setText(R.string.reset);
                    time = new Timer();
                    time.setClock(timeView);
                    status.setText("First Play");
                    time.startTimer();
                    mFirstPlay = !mFirstPlay;
                    } else {
                        status.setText("Second Play");
                        mPlayIcon.setBackgroundResource(R.drawable.ic_pse_mb);
                        time.startTimer();
                    }
                } else {
                    mPlayIcon.setBackgroundResource(R.drawable.ic_ply_mb);
                    time.stopTimer();
                }
                mStartPlaying = !mStartPlaying;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
                mPlayer.setDataSource(mFileName);
            if(Integer.parseInt(timeView.getText().toString()) < mPlayer.getDuration())
            //if(!timeView.toString().equalsIgnoreCase(String.valueOf(mPlayer.getDuration())))
            {
                mPlayer.prepare();  //preparing player to work
                mPlayer.start();
            }
            else {

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}