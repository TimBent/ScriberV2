package com.example.jnrmint.scriber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordingPicker extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> recordingList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_picker);
    }

}
