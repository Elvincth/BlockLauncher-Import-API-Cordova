package io.appery.project409456;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


class AutoUpdateTask extends AsyncTask<Long, Void, String> {

    private final static String TAG = "AutoUpdateTask";

    private final static String GET_BUNDLE_URL = "https://upd.appery.io/update/7c73ac68-0c8a-4ce6-9e6f-a1271b101714.zip";
    
    private final static Long BUILD_TIMESTAMP_MILLISEC = 1465710336 * 1000L;

    private final static String[] JQM_ASSETS_TO_COPY = new String[]{
            "cordova.js",
            "cordova_plugins.js",
            "get_target_platform.js",
            "plugins"
    };

    private final static String[] ANGULAR_ASSETS_TO_COPY = new String[]{
            "cordova.js",
            "cordova_plugins.js",
            "plugins"
    };

    private static final String LIBS_DIR = "www/libs/";

    private static final String CORDOVA_JQM_LIB_DIR = "www/libs/jquerymobile/";

    private final Context context;

    private SharedPreferences app_preferences;

    private static final String WEB_RESOURCES_DIR = "/www/";

    private static final String FILES_DIR = "/files";

    private final static String BUILD_TIME_PREF_NAME = "BuildTime";

    public AutoUpdateTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Long... params) {
        final String baseDirectory = AutoUpdateHelper.DATA_DIR + context.getPackageName();
        try {
            app_preferences =  PreferenceManager.getDefaultSharedPreferences(context);
            Long modifiedDate = app_preferences.getLong(BUILD_TIME_PREF_NAME, BUILD_TIMESTAMP_MILLISEC);

            if (!isConnectionActive()) {
                Log.d(TAG, "No connection");
                return BUILD_TIMESTAMP_MILLISEC.equals(modifiedDate) ? null : baseDirectory + WEB_RESOURCES_DIR;
            }

            URL url = new URL(GET_BUNDLE_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setIfModifiedSince(modifiedDate);
            conn.setReadTimeout(180000);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "The response code is: " + responseCode);

            String workingBundleDir = null;

            if (HttpURLConnection.HTTP_OK == responseCode) {
                InputStream is = conn.getInputStream();
                if (is != null) {
                    AutoUpdateHelper.downloadBundle(is, baseDirectory + FILES_DIR);
                    conn.disconnect();

                    workingBundleDir = AutoUpdateHelper.unzip(baseDirectory + FILES_DIR,
                            baseDirectory + WEB_RESOURCES_DIR);
                }
                String[] assetsToCopy = JQM_ASSETS_TO_COPY;
                File dir = new File(baseDirectory, CORDOVA_JQM_LIB_DIR);
                if (!dir.exists()) {
                    assetsToCopy = ANGULAR_ASSETS_TO_COPY;
                }

                for (String fName : assetsToCopy) {
                    AutoUpdateHelper.copyFileOrDir(context, LIBS_DIR + fName);
                }
            } else {
                return BUILD_TIMESTAMP_MILLISEC.equals(modifiedDate)  ? null : baseDirectory + WEB_RESOURCES_DIR;
            }

            app_preferences.getLong(BUILD_TIME_PREF_NAME, 0);
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putLong(BUILD_TIME_PREF_NAME, conn.getLastModified());
            editor.commit();

            return workingBundleDir;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isConnectionActive(){
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
