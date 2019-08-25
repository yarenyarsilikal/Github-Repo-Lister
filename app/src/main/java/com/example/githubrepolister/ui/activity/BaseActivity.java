package com.example.githubrepolister.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;

    private static final int DEFAULT_DURATION = Toast.LENGTH_SHORT;

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    //region STORY METHODS

    public static void showToast(Context context, String string) {      // method for toasting message
        Toast.makeText(context, string, DEFAULT_DURATION).show();
    }

    //region Loading Progress Dialog Methods
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading ...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    //endregion

    public boolean isNetworkAvailable(Context context) {            //method for checking network connection
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return false;
        }
    }

    //region KeyboardMethods
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (view != null && inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (view != null && inputMethodManager != null) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, 0);
        }
    }
    //endregion
    //endregion
}
