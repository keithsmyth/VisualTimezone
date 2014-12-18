package com.keithsmyth.visualtimezone;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

/**
 * @author keithsmyth
 */
public class Utils {
    /**
     * hides the soft keyboard
     *
     * @param context
     * @param windowToken
     */
    public static void closeKeyboard(Context context, IBinder windowToken) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }
}
