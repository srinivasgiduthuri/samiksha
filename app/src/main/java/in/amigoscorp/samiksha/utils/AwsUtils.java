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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import in.amigoscorp.samiksha.BuildConfig;
import in.amigoscorp.samiksha.constants.Constants;
import in.amigoscorp.samiksha.models.Review;
import okhttp3.OkHttpClient;

/**
 * Created by sriny on 08/02/17.
 */

public class AwsUtils extends AsyncTask<String, Void, Void> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TAG = AwsUtils.class.getSimpleName();
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(BuildConfig.AWS_ACCESS_KEY, BuildConfig.AWS_SECRET_KEY);

    @Override
    protected Void doInBackground(String... params) {
        AmazonS3 amazonS3 = new AmazonS3Client(AWS_CREDENTIALS);
        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        try {
            S3ObjectInputStream reviewsInputStream = amazonS3.getObject(new GetObjectRequest("samiksha", "reviews.json")).getObjectContent();
            //List<String> reviewsLines = IOUtils.readLines(reviewsInputStream, Charset.forName("UTF-8"));
            //String rawReviews = StringUtils.join(reviewsLines, " ");
            Constants.reviews = OBJECT_MAPPER.readValue(reviewsInputStream, new TypeReference<List<Review>>() {
            });
            reviewsInputStream.close();
            S3ObjectInputStream upcomingInputStream = amazonS3.getObject(new GetObjectRequest("samiksha", "upcoming.json")).getObjectContent();
            //List<String> upcomingLines = IOUtils.readLines(upcomingInputStream, Charset.forName("UTF-8"));
            //String rawUpcoming = StringUtils.join(upcomingLines, " ");
            Constants.upcoming = OBJECT_MAPPER.readValue(upcomingInputStream, new TypeReference<List<Review>>() {
            });
            reviewsInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        /*try {
            Request reviewsRequest = new Request.Builder().url("https://s3-ap-southeast-1.amazonaws.com/samiksha/reviews.json").build();
            Response reviewsResponse = CLIENT.newCall(reviewsRequest).execute();
            String rawReviews = reviewsResponse.body().string();
            Constants.reviews = OBJECT_MAPPER.readValue(rawReviews, new TypeReference<List<Review>>() {
            });
            Request upcomingRequest = new Request.Builder().url("https://s3-ap-southeast-1.amazonaws.com/samiksha/upcoming.json").build();
            Response upcomingResponse = CLIENT.newCall(upcomingRequest).execute();
            String rawUpcoming = upcomingResponse.body().string();
            Constants.upcoming = OBJECT_MAPPER.readValue(rawUpcoming, new TypeReference<List<Review>>() {
            });
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }*/
        return null;
    }
}
