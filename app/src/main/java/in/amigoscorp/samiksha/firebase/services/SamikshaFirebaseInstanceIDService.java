package in.amigoscorp.samiksha.firebase.services;

import com.google.firebase.iid.FirebaseInstanceIdService;

import in.amigoscorp.samiksha.utils.CommonUtils;
import in.amigoscorp.samiksha.utils.FirebaseTokenUploader;

/**
 * Created by sriny on 23/02/17.
 */

public class SamikshaFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "SamikshaFIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        CommonUtils.firebaseToken();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        CommonUtils.sendRegistrationToServer(getApplicationContext());
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     */
    private void sendRegistrationToServer() {
        FirebaseTokenUploader firebaseTokenUploader = new FirebaseTokenUploader(getApplicationContext());
        firebaseTokenUploader.execute();
    }
}
