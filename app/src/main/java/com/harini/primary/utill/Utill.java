package com.harini.primary.utill;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Utill {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}
