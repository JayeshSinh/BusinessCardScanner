package com.business.card.scanner.maker.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.business.card.scanner.maker.App;


public class AppPref {
    static final String APP_LOCK = "app_lock";
    static final String AUTO_SAVE = "Auto_save";
    static final String CARD_DISPLAY_ORDER = "card_dispaly_order";
    static final String GROUP_CHOOSE = "Group_choose";
    static final String IS_ADFREE = "IS_ADFREE";
    static final String IS_DEFAULT = "isDefault";
    static final String IS_DISCLAIMER_ACCEPT = "IS_DISCLAIMER_ACCEPT";
    static final String IS_RATEUS = "IS_RATEUS";
    static final String IS_RATE_US = "IS_RATE_US";
    static final String IS_RATE_US_NEW = "IS_RATE_US_NEW";
    static final String IS_TERMS_ACCEPT = "isTermsAccept";
    static final String LANGUAGE = "language";
    static final String LAST_FIRST = "last_first";
    static final String MyPref = "userPref";

    public static boolean IsRateUs(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_RATEUS, false);
    }

    public static void setRateUs(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_RATEUS, z);
        edit.commit();
    }

    public static boolean IsDisclaimerAccept(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_DISCLAIMER_ACCEPT, false);
    }

    public static boolean isLast_first(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(LAST_FIRST, false);
    }

    public static void setLastFirst(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(LAST_FIRST, z);
        edit.commit();
    }

    public static boolean isAuto_save(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(AUTO_SAVE, false);
    }

    public static void setAutoSave(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(AUTO_SAVE, z);
        edit.commit();
    }

    public static boolean isGroup_choose(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(GROUP_CHOOSE, false);
    }

    public static void setGroup_choose(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(GROUP_CHOOSE, z);
        edit.commit();
    }

    public static void setCardDisplayOrder(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(CARD_DISPLAY_ORDER, z);
        edit.apply();
        edit.commit();
    }

    public static boolean isCardDispalyOrder(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(CARD_DISPLAY_ORDER, false);
    }

    public static void setAppLock(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(APP_LOCK, z);
        edit.apply();
        edit.commit();
    }

    public static boolean isAppLock(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(APP_LOCK, false);
    }

    public static boolean IsTermsAccept(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_TERMS_ACCEPT, false);
    }

    public static void setIsTermsAccept(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_TERMS_ACCEPT, z);
        edit.commit();
    }

    public static void setIsDefault(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_DEFAULT, z);
        edit.apply();
        edit.commit();
    }

    public static boolean isDefault(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_DEFAULT, false);
    }

    public static void setLanguage(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(LANGUAGE, str);
        edit.apply();
        edit.commit();
    }

    public static String getLanguage(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(LANGUAGE, "English");
    }

    public static boolean IsRateUsNew(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_RATE_US_NEW, false);
    }

    public static void setIsRateUsNew(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_RATE_US_NEW, z);
        edit.commit();
    }

    public static boolean getIsAdfree() {
        return App.getContext().getSharedPreferences(MyPref, 0).getBoolean(IS_ADFREE, false);
    }

    public static void setIsAdfree(boolean z) {
        SharedPreferences.Editor edit = App.getContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_ADFREE, z);
        edit.commit();
    }
}
