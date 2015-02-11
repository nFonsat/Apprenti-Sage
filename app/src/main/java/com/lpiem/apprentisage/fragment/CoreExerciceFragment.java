/**
 * Created by Nicolas on 01/02/2015.
 */
package com.lpiem.apprentisage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.lpiem.apprentisage.Utils.Consts;
import com.lpiem.apprentisage.metier.Exercice;

import java.util.Locale;

public class CoreExerciceFragment extends Fragment {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + CoreExerciceFragment.class.getSimpleName();

    protected FragmentAccess mFragmentAccess;
    protected Exercice mCurrentExercice;
    protected TextToSpeech mTextToSpeech;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mTextToSpeech = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    mTextToSpeech.setLanguage(Locale.FRANCE);
                }
            });

            mFragmentAccess = (FragmentAccess) activity;
        } catch (ClassCastException e) {
            String error = activity.toString() + " must implement FragmentAccess";
            Log.e(LOG + " : Error", error);
            throw new ClassCastException(error);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mTextToSpeech != null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }

    protected void setParameter(Exercice exercice){
        mCurrentExercice = exercice;
    }

    protected boolean isNumeric(String number){
        try{
            Long.parseLong(number);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    protected void speaker(String phrase){
        Log.d(LOG + " : Build.VERSION.SDK_INT", String.valueOf(Build.VERSION.SDK_INT));
        if(Build.VERSION.SDK_INT < 21){
            mTextToSpeech.speak(phrase, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            CharSequence charSequence = phrase;
            mTextToSpeech.speak(charSequence,TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}