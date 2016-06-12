package io.appery.project409456;

import android.os.Bundle;
import org.apache.cordova.*;
import android.util.Log;

public class MainActivity extends CordovaActivity
{
    private final static String TAG = "MainActivity";
    
    private final boolean isAutoupdateEnabled = true;

    private static final String WORK_DIR = "file:///android_asset/www/";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String startFileName = null;

        if (isAutoupdateEnabled) {
            AutoUpdateTask autoUpdateTask = new AutoUpdateTask(this);
            autoUpdateTask.execute();
            try {
                startFileName = autoUpdateTask.get();
            } catch (Exception e) {
                Log.d(TAG, "Autoupdate failed!");
                e.printStackTrace();
            }
        }
        String filePath = (startFileName != null ? "file://" + startFileName : WORK_DIR) + "index.html";
        loadUrl(filePath);
    }
    
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
