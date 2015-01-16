package com.lpiem.apprentisage.Utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lpiem.apprentisage.R;

import java.util.Locale;

/**
 * Created by iem on 16/01/15.
 */
public class TextToSpeechUtils {

    private TextToSpeech textToSpeech;

    public void speakText(Context context, String enoncer){
        String toSpeak = enoncer;

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != textToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.FRANCE);
                }
            }
        });

        Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show();
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

}
