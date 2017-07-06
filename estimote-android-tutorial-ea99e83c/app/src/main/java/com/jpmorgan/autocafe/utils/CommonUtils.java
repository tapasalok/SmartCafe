package com.jpmorgan.autocafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tapas on 6/21/2017.
 */

public class CommonUtils {
    private static CommonUtils commonUtils;
    private static Context context;
    private static SharedPreferences sharedPreferences;

    public static CommonUtils getInstance(Context context1) {
        context = context1;
        if (commonUtils == null) {
            commonUtils = new CommonUtils();
        }

        sharedPreferences = context.getSharedPreferences("auto_cafe", 0);

        return commonUtils;
    }

    public static void setVendorLogin(final boolean vendorLogin) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("vendorLogin", vendorLogin);
            editor.commit();
        }
    }

    public static boolean isVendorLogin() {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean("vendorLogin", false);
        } else {
            return false;
        }
    }

}
