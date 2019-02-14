package com.developer.manuelquinteros.clinicadentalx.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class persistenceField {


    private String PREFS_KEY = "campos";

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String KEY_COD = "EditTextCode";
    public static final String KEY_DAT = "EditTextDate";
    public static final String KEY_PER = "EditTextPersona";

    public persistenceField(Context ctx) {
        this._context = ctx;
        pref = _context.getSharedPreferences(PREFS_KEY, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveCODE(String cod){
        editor = pref.edit();
        editor.putString(KEY_COD, cod);
        editor.commit();
    }

    public void saveDATE(String dat){
        editor = pref.edit();
        editor.putString(KEY_DAT, dat);
        editor.commit();
    }

    public void savePERSONA(String per){
        editor = pref.edit();
        editor.putString(KEY_PER, per);
        editor.commit();
    }

    public HashMap<String, String> getValueField() {
        HashMap<String, String> field = new HashMap<String, String>();
        field.put(KEY_COD, pref.getString(KEY_COD, null));
        field.put(KEY_DAT, pref.getString(KEY_DAT, null));
        field.put(KEY_PER, pref.getString(KEY_PER, null));
        return field;
    }

    public void cleanField(){
        editor = pref.edit();
        editor.clear().commit();
    }
}
