package com.business.card.scanner.maker.baseClass;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.business.card.scanner.maker.listener.OnFragmentInteractionListener;

public abstract class BaseFragmentBinding extends Fragment implements View.OnClickListener {
    public Context context;
    private OnFragmentInteractionListener mListener;


    public abstract View getViewBinding();


    public abstract void initMethods();


    public abstract void initVariable();


    public abstract void setAdapter();


    public abstract void setBinding(LayoutInflater layoutInflater, ViewGroup viewGroup);


    public abstract void setOnClicks();


    public abstract void setToolbar();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getActivity();
        setBinding(layoutInflater, viewGroup);
        setToolbar();
        initVariable();
        setOnClicks();
        initMethods();
        setAdapter();
        return getViewBinding();
    }

    public void onButtonPressed(Uri uri) {
        OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context2) {
        super.onAttach(context2);
        if (context2 instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context2;
            return;
        }
        throw new RuntimeException(context2.toString() + " must implement OnFragmentInteractionListener");
    }
}
