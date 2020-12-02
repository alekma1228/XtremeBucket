package com.brainyapps.xtremebucketlist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.brainyapps.xtremebucketlist.model.BucketModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AppPreference {
    private static SharedPreferences instance = null;
    public static final String TAG = "AppPreference";
    private String DIVKEY = "#&#";

    public static final String MY_EMAIL = "my_email";
    public static final String FIRST_STEP = "first_step";
    public static final String FIRST_SIGNUP = "FIRST_SIGNUP";

    public static final int LOOKING_FOR_WORK = 0x01;
    public static final int I_HAVE_WORK = 0x02;
    public static final int ADMIN = 0x03;

    private static String APP_SHARED_PREFS;
    private static SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefsEditor;

    public static class KEY {
        // sign in
        public static final String SIGN_IN_STAY = "SIGN_IN_STAY";
        public static final String SIGN_IN_AUTO = "SIGN_IN_AUTO";
        public static final String SIGN_IN_USERNAME = "SIGN_IN_USERNAME";
        public static final String SIGN_IN_PASSWORD = "SIGN_IN_PASSWORD";
        public static final String SIGN_UP_USERNAME = "SIGN_UP_USERNAME";
        public static final String SIGN_UP_PASSWORD = "SIGN_UP_PASSWORD";
        public static final String SIGN_UP_CONFIRM_PASSWORD = "SIGN_UP_CONFIRM_PASSWORD";
        public static final String AGREE = "AGREE";
        public static final String MESSAGE_COUNT = "MESSAGE_COUNT";

        public static final String USER_TYPE = "USER_TYPE";

        public static final String SELECTED_TOURNAMENT = "SELECTED_TOURNAMENT";
        public static final String SELECTED_CHALLENGE = "SELECTED_CHALLENGE";

        // camera setting
        public static final String CAMERA_RESOLUTION_INDEX = "camera_resolution_index";

        public static final String ACTIVITY_DATA = "ACTIVITY_DATA";
        public static final String LOOKOUT_DATA = "LOOKOUT_DATA";

        public static final String NOTIFICATION_COUNT = "NOTIFICATION_COUNT";

        public static final String BUCKET_LIST = "BUCKET_LIST";
    }

    public static void initialize(SharedPreferences pref) {
        instance = pref;
    }

    // boolean
    public static boolean getBool(String key, boolean def) {
        return instance.getBoolean(key, def);
    }
    public static void setBool(String key, boolean value) {
        SharedPreferences.Editor editor = instance.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    // int
    public static int getInt(String key, int def) {
        return instance.getInt(key, def);
    }
    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = instance.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // long
    public static long getLong(String key, long def) {
        return instance.getLong(key, def);
    }
    public static void setLong(String key, long value) {
        SharedPreferences.Editor editor = instance.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    // string
    public static String getStr(String key, String def) {
        return instance.getString(key, def);
    }
    public static void setStr(String key, String value) {
        SharedPreferences.Editor editor = instance.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // remove
    public static void removeKey(String key) {
        SharedPreferences.Editor editor = instance.edit();
        editor.remove(key);
        editor.commit();
    }

    public AppPreference(Context context) {
        APP_SHARED_PREFS = context.getApplicationContext().getPackageName();
        mPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
                Activity.MODE_PRIVATE);
        mPrefsEditor = mPrefs.edit();
    }

    public void saveMyEmail(String email) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(MY_EMAIL, email);
        editor.commit();
    }

    public String getMyEmail() {
        String ret = mPrefs.getString(MY_EMAIL, "");
        return ret;
    }

    public void saveFirstStep() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(FIRST_STEP, true);
        editor.commit();
    }

    public boolean getFirstStep() {
        boolean ret = mPrefs.getBoolean(FIRST_STEP, false);
        return ret;
    }

    public void saveFirstSignUp() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(FIRST_SIGNUP, true);
        editor.commit();
    }

    public boolean getFirstSignUp() {
        boolean ret = mPrefs.getBoolean(FIRST_SIGNUP, false);
        return ret;
    }

    public void setSelectedTournamentID(String tournamentId){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY.SELECTED_TOURNAMENT, tournamentId);
        editor.commit();
    }

    public String getSelectedTournamentID(){
        String ret = mPrefs.getString(KEY.SELECTED_TOURNAMENT, "");
        return ret;
    }
    public void setSelectedChallengeID(String tournamentId){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY.SELECTED_CHALLENGE, tournamentId);
        editor.commit();
    }

    public String getSelectedChallengeID(){
        String ret = mPrefs.getString(KEY.SELECTED_CHALLENGE, "");
        return ret;
    }

    public void setLookOut(ArrayList<Long> arrayList){
        String value = "";
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i< arrayList.size(); i++){
            jsonArray.put(arrayList.get(i));
        }
        value = jsonArray.toString();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY.LOOKOUT_DATA, value);
        editor.commit();
    }
    public ArrayList<Long> getLookOut(){
        ArrayList<Long> ret = new ArrayList<>();
        String data = mPrefs.getString(KEY.LOOKOUT_DATA, "");
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i=0; i<jsonArray.length(); i++){
                ret.add(jsonArray.getLong(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void setBucketListData(ArrayList<BucketModel> arrayList){
        String value = "";
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arrayList.size(); i++){
            BucketModel bucketModel= arrayList.get(i);
            jsonArray.put(bucketModel.toJsonString());
        }

        value = jsonArray.toString();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY.BUCKET_LIST, value);
        editor.commit();
    }
    public void setBucketListDataString(String value){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY.BUCKET_LIST, value);
        editor.commit();
    }
    public ArrayList<BucketModel> getBucketListData(){
        ArrayList<BucketModel> ret = new ArrayList<>();
        String data = mPrefs.getString(KEY.BUCKET_LIST, "");
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i=0; i<jsonArray.length(); i++){
                BucketModel bucketModel = BucketModel.getBucketModelFromJsonStr(jsonArray.getString(i));
                if (bucketModel != null)
                    ret.add(bucketModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
    public String getBucketListDataString(){
        String data = mPrefs.getString(KEY.BUCKET_LIST, "");
        return data;
    }
}


