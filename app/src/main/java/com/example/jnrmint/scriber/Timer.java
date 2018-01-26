package com.example.jnrmint.scriber;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Created by tim-b on 25/01/2018.
 */

public class Timer {

    private long startHTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private Runnable updateTimerThread;

    public void startTimer(){
        startHTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void stopTimer(){
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    public void setClock(final TextView text){
        updateTimerThread = new Runnable() {
            final TextView txt;
            {
                txt = text;
            }

            public void run() {
                timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                if (txt != null)
                    txt.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            }
        };
    }
}
