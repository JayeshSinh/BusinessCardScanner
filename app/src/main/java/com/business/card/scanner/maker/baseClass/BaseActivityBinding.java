package com.business.card.scanner.maker.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivityBinding extends AppCompatActivity implements View.OnClickListener {
    public Context context;


    public abstract void initMethods();


    public abstract void initVariable();


    public abstract void setAdapter();


    public abstract void setBinding();


    public abstract void setOnClicks();


    public abstract void setToolbar();


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setBinding();
        initVariable();
        setToolbar();
        setOnClicks();
        initMethods();
        setAdapter();
    }
}
