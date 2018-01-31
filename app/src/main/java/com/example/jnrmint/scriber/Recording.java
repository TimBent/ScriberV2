package com.example.jnrmint.scriber;

import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * Created by tim-b on 31/01/2018.
 */

public class Recording extends MediaRecorder{


    private static final String LOG_TAG = "AudioRecordTest";
    private static MediaRecorder mRecorder;
    private static String mFileName = null;
    private boolean mFirstRecord;

    protected void onRecord(boolean start) {
        if (start) {
            if(mFirstRecord) startRecording();
            else mRecorder.start();
        } else {
            pauseRecording();
        }
    }

    public void setFileName(String fn){
        mFileName = fn;
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

    protected void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void pauseRecording(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mRecorder.pause();
        }
    }

    public void setmFirstRecord(Boolean mFR) {
        mFirstRecord = mFR;
    }
}
