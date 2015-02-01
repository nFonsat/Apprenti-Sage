/**
 * Created by Nicolas on 01/02/2015.
 */
package com.lpiem.apprentisage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.metier.Exercice;

public class CoreExerciceFragment extends Fragment {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + CoreExerciceFragment.class.getSimpleName();

    protected FragmentAccess mFragmentAccess;
    protected Exercice mCurrentExercice;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentAccess = (FragmentAccess) activity;
        } catch (ClassCastException e) {
            String error = activity.toString() + " must implement FragmentAccess";
            Log.e(LOG + " : Error", error);
            throw new ClassCastException(error);
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
}