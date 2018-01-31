package com.example.jnrmint.scriber;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
    public boolean mFirstRecord = true;

    private boolean mStopped = false;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;
    private static String sFileName = null;

    private ImageButton mRecIcon = null;
    //private MediaRecorder mRecorder = null;
    private Recording mRecorder = new Recording();


    private ImageButton mPlayIcon = null;
    private ImageButton mStopIcon = null;
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
        mFileName += "/audiorecordtest.3gp";
        mRecorder.setFileName(mFileName);


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //Timer initiation
        timeView = findViewById(R.id.timer);
        reset = timeView;
        time = new Timer();
        time.setClock(timeView);

        status = findViewById(R.id.output);

        mRecIcon = findViewById(R.id.playpausebtn);
        mRecIcon.setBackgroundResource(R.drawable.ic_strt_rec);
        mRecIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStartPlaying) {
                    mRecorder.onRecord(mStartRecording);
                    mRecorder.setmFirstRecord(mFirstRecord);
                    if (mStartRecording) {
                        if(mFirstRecord) {
                            mRecIcon.setBackgroundResource(R.drawable.ic_stp_rec);
                            resetTime();
                            status.setText("Recording started");
                            time.startTimer();
                            mFirstRecord = !mFirstRecord;
                        } else {
                            mRecIcon.setBackgroundResource(R.drawable.ic_stp_rec);
                            status.setText("Recording resumed");
                            time.startTimer();
                        }
                    } else {
                        mRecIcon.setBackgroundResource(R.drawable.ic_strt_rec);
                        status.setText("Recording Paused");
                        time.stopTimer();
                    }
                    mStartRecording = !mStartRecording;
                }   else {
                    status.setText("Cannot record whilst playing!");
                }
            }
        });

        mPlayIcon = findViewById(R.id.recstopBtn);
        mPlayIcon.setBackgroundResource(R.drawable.ic_ply_mb);
        mPlayIcon.setOnClickListener(new View.OnClickListener() {
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
                                mPlayIcon.setBackgroundResource(R.drawable.ic_pse_mb);
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
        });

        mStopIcon = findViewById(R.id.stopplayBtn);
        mStopIcon.setBackgroundResource(R.drawable.ic_stp_mb);
        mStopIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("All media stopped!");
                time.stopTimer();
                resetTime();
                if(!mStartRecording) {
                    mRecorder.stopRecording();
                    mStartRecording = true;
                    mFirstRecord = true;
                    mRecIcon.setBackgroundResource(R.drawable.ic_strt_rec);
                }
                if (!mStartPlaying)  {
                    stopPlaying();
                    mStartPlaying = true;
                    mFirstPlay = true;
                    mPlayIcon.setBackgroundResource(R.drawable.ic_ply_mb);
                }
            }
        });
    }

    public void resetTime(){
        timeView.setText(R.string.reset);
        time = new Timer();
        time.setClock(timeView);
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



    private void onPlay(boolean start) {
        if (start) {
            if(mFirstPlay) startPlaying();
            else mPlayer.start();
        } else {
            pausePlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
                mPlayer.setDataSource(mFileName);
                //if(!timeView.toString().equalsIgnoreCase(String.valueOf(mPlayer.getDuration())))
                //{
                mPlayer.prepare();  //preparing player to work
                mPlayer.start();
                //}
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void pausePlaying(){
        mPlayer.pause();
    }


    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
    /*
    private void onRecord(boolean start) {
        if (start) {
            if(mFirstRecord) startRecording();
            else mRecorder.start();
        } else {
            pauseRecording();
        }
    }
    private void pauseRecording(){
        mRecorder.pause();
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
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
    }*/

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