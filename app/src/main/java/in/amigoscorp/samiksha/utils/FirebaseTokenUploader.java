package in.amigoscorp.samiksha.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import in.amigoscorp.samiksha.BuildConfig;

import static android.content.Context.MODE_PRIVATE;
import static in.amigoscorp.samiksha.constants.Constants.OBJECT_MAPPER;
import static in.amigoscorp.samiksha.constants.Constants.androidId;
import static in.amigoscorp.samiksha.constants.Constants.user;

/**
 * Created by sriny on 23/02/17.
 */

public class FirebaseTokenUploader extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private static final String TAG = FirebaseTokenUploader.class.getSimpleName();
    private static final AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(BuildConfig.AWS_ACCESS_KEY, BuildConfig.AWS_SECRET_KEY);

    public FirebaseTokenUploader(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AmazonS3 amazonS3 = new AmazonS3Client(AWS_CREDENTIALS);
        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        File file = null;
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(BuildConfig.AWS_USERS_OBJECT_KEY, MODE_PRIVATE);
            OBJECT_MAPPER.writeValue(fileOutputStream, user);
            fileOutputStream.close();
            file = context.getFileStreamPath(BuildConfig.AWS_USERS_OBJECT_KEY);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("application/json");
            PutObjectRequest putObjectRequest = new PutObjectRequest(BuildConfig.AWS_SAMIKSHA_USERS_BUCKET_NAME, androidId, fileInputStream, objectMetadata);
            amazonS3.putObject(putObjectRequest);
            fileInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Unable to create JSON for the user object.");
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return null;
    }
}
