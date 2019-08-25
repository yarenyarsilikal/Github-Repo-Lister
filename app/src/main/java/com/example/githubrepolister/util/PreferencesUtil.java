package com.example.githubrepolister.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferencesUtil {

    private static final String KEY_SHARED_FILE = "APP_FILE";

    private final SharedPreferences sharedPreferences;


    public PreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_SHARED_FILE, Context.MODE_PRIVATE);
    }

    public String getData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void saveData(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void removeData(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
    public void saveRepoIdList(String key, List<String> list) {
        sharedPreferences.edit().putString(key, new Gson().toJson(list)).apply();
    }

    public List<String> getRepoIdList(String key) {
        String allUserData = getData(key, null);
        if (allUserData == null)
            return null;

        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(allUserData, listType);
    }
}
