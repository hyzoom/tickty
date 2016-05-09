package com.ishraq.gosport;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by hp on 16/12/2015.
 */
public class ParseApp extends Application {
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "AD8TpKZmFuxwIPdgiFAMSVMvM6bjn4gWQqC10MQn", "wab34cSHVkEDoMjFwPXvlsBbf644Xqa30dvQpRJg");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    }
}
