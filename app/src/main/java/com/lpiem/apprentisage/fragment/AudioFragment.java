/**
 * Created by Nicolas on 14/01/2015.
 */
package com.lpiem.apprentisage.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Utils.TextToSpeechUtils;
import com.lpiem.apprentisage.metier.Exercice;

public class AudioFragment extends Fragment {
    private Button mPlayEnonce;

    private Context context;
    private Exercice mCurrentExercice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_text, container, false);

        mPlayEnonce = (Button) view.findViewById(R.id.lecture_btn_player);
        this.context = view.getContext();

        mPlayEnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentExercice != null){
                    TextToSpeechUtils.speakText(context, mCurrentExercice.getEnonce());
                }
            }
        });

        return view;
    }

    public void setParameter(Exercice exercice){
        mCurrentExercice = exercice;
    }
}