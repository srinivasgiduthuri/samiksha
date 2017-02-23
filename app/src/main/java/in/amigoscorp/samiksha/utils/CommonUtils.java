package in.amigoscorp.samiksha.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;

import in.amigoscorp.samiksha.BuildConfig;
import in.amigoscorp.samiksha.R;

import static in.amigoscorp.samiksha.constants.Constants.androidId;
import static in.amigoscorp.samiksha.constants.Constants.user;

/**
 * Created by sriny on 16/02/17.
 */

public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();

    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }

        return false;
    }

    public static void subscribeTopicViaFirebase() {
        FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.FCM_TOPIC);
    }

    public static void androidId(final ContentResolver contentResolver) {
        if (StringUtils.isBlank(androidId)) {
            androidId = android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID);
        }
    }

    public static void firebaseToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        user.setAndroidId(androidId);
        user.setFirebaseToken(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    public static void sendRegistrationToServer(Context context) {
        FirebaseTokenUploader firebaseTokenUploader = new FirebaseTokenUploader(context);
        firebaseTokenUploader.execute();
    }

    public static void writeToSharedPreferences(Context context, int key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(key), value);
        editor.apply();
    }

    public static int readFromSharedPreferences(Context context, int key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(key), 1);
    }

    public static Bitmap getBitmapFromImageUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
}
