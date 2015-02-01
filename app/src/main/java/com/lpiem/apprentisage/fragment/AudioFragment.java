/**
 * Created by Nicolas on 14/01/2015.
 */
package com.lpiem.apprentisage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.metier.Exercice;

public class AudioFragment extends CoreExerciceFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercice_audio, container, false);

        Button mPlayEnonce = (Button) view.findViewById(R.id.lecture_btn_player);
        mPlayEnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentExercice != null){/*TextToSpeech*/}
            }
        });

        return view;
    }

    public void setParameter(Exercice exercice){
        super.setParameter(exercice);
    }
}