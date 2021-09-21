package com.business.card.scanner.maker;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context context;
    private static App mInstance;
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }


    public void attachBaseContext(Context context2) {
        super.attachBaseContext(context2);
    }

    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mInstance = this;
        context = getApplicationContext();
    }

}
