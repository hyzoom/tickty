package com.ishraq.gosport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMRegistrar;

/**
 * Created by hp on 11/12/2015.
 */
public class SplashScreen extends Activity {
    Thread splashTread;
    protected boolean _active = true;
    protected int _splashTime = 10;
    private boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_row);
        final String regId = GCMRegistrar.getRegistrationId(this);
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                } finally {
                    if (!stop) {
                        if (regId.equals("")) {
                            Intent i = new Intent(SplashScreen.this, RegisterActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashScreen.this, FeedListActivity.class);
                            startActivity(i);
                        }
                        finish();
                    } else
                        finish();
                }
            }
        }

        ;
        splashTread.start();

    }
}
