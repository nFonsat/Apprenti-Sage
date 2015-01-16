/**
 * Created by Nicolas on 14/01/2015.
 */
package com.lpiem.apprentisage.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.metier.Exercice;

public class TextFragment extends Fragment {
    private TextView mEnonce;
    private EditText mReponse;

    private Exercice mCurrentExercice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_text, container, false);

        mEnonce = (TextView) view.findViewById(R.id.enonce_txt);
        mReponse = (EditText) view.findViewById(R.id.reponse_txt);

        if(mCurrentExercice != null){
            mEnonce.setText(mCurrentExercice.getEnonce());
        }

        return view;
    }

    public void setParameter(Exercice exercice){
        mCurrentExercice = exercice;
    }

    public String getResponse(){
        return mReponse.getText().toString();
    }
}