package org.thoughtcrime.securesms.gcm;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.signal.core.util.logging.Log;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public final class FcmUtil {

  private static final String TAG = Log.tag(FcmUtil.class);

  /**
   * Retrieves the current FCM token. If one isn't available, it'll be generated.
   */
  @WorkerThread
  public static Optional<String> getToken(Context context) {
    String token = null;

    String googleAppId = "1:306971810405:android:340cb2ff490fc73b30048c";
    String gcmDefaultSenderId = "306971810405";
    String defaultWebClientId = "306971810405-up1i5m2vth6u5c5h252q3m2m4gouf23a.apps.googleusercontent.com";
    String firebaseDatabaseUrl = "https://kahf-376320.appspot.com";
    String googleApiKey = "AIzaSyCpOQYtl7Sc6RphkUpIT76g9BgP2AZ417U";
    String googleCrashReportingApiKey = "AIzaSyCpOQYtl7Sc6RphkUpIT76g9BgP2AZ417U";
    String projectId = "kahf-376320";

    FirebaseOptions firebaseOptions = new FirebaseOptions.Builder().setApplicationId(googleAppId).
            setGcmSenderId(gcmDefaultSenderId).
            setDatabaseUrl(firebaseDatabaseUrl).
            setApiKey(googleApiKey).
            setProjectId(projectId).
            build();

    // Must be called manually if running outside of main process
    FirebaseMessaging firebaseMessaging = (FirebaseMessaging) FirebaseApp.initializeApp(context, firebaseOptions, "messaging").get(FirebaseMessaging.class);

    try {
      token = Tasks.await(firebaseMessaging.getToken());
    } catch (InterruptedException e) {
      Log.w(TAG, "Was interrupted while waiting for the token.");
    } catch (ExecutionException e) {
      Log.w(TAG, "Failed to get the token.", e.getCause());
    }

    return Optional.ofNullable(TextUtils.isEmpty(token) ? null : token);
  }
}
