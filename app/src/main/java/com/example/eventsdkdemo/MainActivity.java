package com.example.eventsdkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.cifrasoft.MediatagSDK.MediatagEvent;
import com.cifrasoft.MediatagSDK.Mediatag;
import com.example.eventsdkdemo.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private int selectedContactTypeIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Mediatag.instance(this).prepare("partner_name");
        Mediatag.instance(this).prepare("partner_name", "ThemSectionId", "HardID");

        String[] contactTypes = {getString(R.string.undefined), getString(R.string.live_broadcast),
                getString(R.string.vod), getString(R.string.catch_up),
                getString(R.string.text_article), getString(R.string.soc_network),
                getString(R.string.live_audio), getString(R.string.audio)};

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, contactTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.contactType.setAdapter(adapter);
        binding.contactType.setText(adapter.getItem(selectedContactTypeIndex), false);
        binding.btnStart.setChecked(true);


        binding.contactType.setOnItemClickListener((adapterView, view1, i, l) -> {
            selectedContactTypeIndex = i;
        });


        binding.btnSendEvent.setOnClickListener(view1 -> {
            Log.i(TAG, binding.toggleGroupType.getCheckedButtonId() + " " + binding.contactType.getListSelection());
            View parentLayout = findViewById(android.R.id.content);
            if(binding.tvFrameTs.getEditableText().length() > 0 &&  binding.tvCuId.getEditableText().length() > 0
                    && binding.tvCatId.getEditableText().length() > 0) {
                int selectedViewTypeIndex = 0;
                if(binding.btnStart.isChecked())
                    selectedViewTypeIndex = 1;
                else if(binding.btnHeartbeat.isChecked())
                    selectedViewTypeIndex = 2;
                else if(binding.btnPause.isChecked())
                    selectedViewTypeIndex = 3;

                MediatagEvent event = new MediatagEvent(Integer.valueOf(String.valueOf(binding.tvCatId.getEditableText())),
                        binding.tvCuId.getEditableText().toString(),
                        MediatagEvent.ContactTypes.values()[selectedContactTypeIndex],
                        MediatagEvent.ViewTypes.values()[selectedViewTypeIndex],
                        Long.valueOf(binding.tvFrameTs.getEditableText().toString()));


                if (binding.tvCuVer.getEditableText().length() > 0)
                    event.setCuVer(Integer.valueOf(binding.tvCuVer.getEditableText().toString()));

                if (binding.tvMedia.getEditableText().length() > 0)
                    event.setMedia(binding.tvMedia.getEditableText().toString());

                if (binding.tvCuUrl.getEditableText().length() > 0)
                    event.setCuUrl(binding.tvCuUrl.getEditableText().toString());

                Mediatag.instance().addEventObject(event);

                Snackbar.make(parentLayout, R.string.event_send, Snackbar.LENGTH_LONG).show();
            }
            else
            {
                Snackbar.make(parentLayout, R.string.event_not_send, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Mediatag.release();
    }
}