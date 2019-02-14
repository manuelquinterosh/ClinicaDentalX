package com.developer.manuelquinteros.clinicadentalx.prefs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.developer.manuelquinteros.clinicadentalx.LoginActivity;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "codontic";

    private static final String IS_USER_LOGIN = "IsUserLogedIn";

    public static final String KEY_USER = "usuario";

    public static final String KEY_NAME = "nombre";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_PHONE = "telefono";

    public static final String KEY_ADDRESS = "direccion";

    public static final String KEY_PASSWORD = "contrasena";

    public static final String KEY_ID = "idUser";

    public UserSessionManager(Context ctx) {
        this._context = ctx;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String user, String nombre, String email, String telefono, String direccion, String contrasena, String id) {

        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_USER, user);

        editor.putString(KEY_NAME, nombre);

        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_PHONE, telefono);

        editor.putString(KEY_ADDRESS, direccion);

        editor.putString(KEY_PASSWORD, contrasena);

        editor.putString(KEY_ID, id);

        editor.commit();
    }

    public boolean isUserLogedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean checkLogin() {

        if (!this.isUserLogedIn()) {

            Intent i = new Intent(_context, LoginActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

            return true;
        }

        return false;
    }

    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER, pref.getString(KEY_USER, null));

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));

        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        user.put(KEY_ID, pref.getString(KEY_ID, null));

        return user;
    }

    public void logout() {

        editor.clear();

        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

}
