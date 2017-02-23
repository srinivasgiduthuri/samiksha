package in.amigoscorp.samiksha.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;

import in.amigoscorp.samiksha.BuildConfig;
import in.amigoscorp.samiksha.constants.Constants;
import in.amigoscorp.samiksha.models.Review;

import static in.amigoscorp.samiksha.constants.Constants.OBJECT_MAPPER;

/**
 * Created by sriny on 08/02/17.
 */

public class SamikshaContentDownloader extends AsyncTask<String, Void, Void> {
    private static final String TAG = SamikshaContentDownloader.class.getSimpleName();
    private static final AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(BuildConfig.AWS_ACCESS_KEY, BuildConfig.AWS_SECRET_KEY);

    @Override
    protected Void doInBackground(String... params) {
        AmazonS3 amazonS3 = new AmazonS3Client(AWS_CREDENTIALS);
        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        try {
            S3ObjectInputStream reviewsInputStream = amazonS3.getObject(new GetObjectRequest(BuildConfig.AWS_SAMIKSHA_BUCKET_NAME, BuildConfig.AWS_REVIEWS_OBJECT_KEY)).getObjectContent();
            Constants.reviews = OBJECT_MAPPER.readValue(reviewsInputStream, new TypeReference<List<Review>>() {
            });
            reviewsInputStream.close();
            S3ObjectInputStream upcomingInputStream = amazonS3.getObject(new GetObjectRequest(BuildConfig.AWS_SAMIKSHA_BUCKET_NAME, BuildConfig.AWS_UPCOMING_OBJECT_KEY)).getObjectContent();
            Constants.upcoming = OBJECT_MAPPER.readValue(upcomingInputStream, new TypeReference<List<Review>>() {
            });
            reviewsInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
