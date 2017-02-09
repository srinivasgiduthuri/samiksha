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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import in.amigoscorp.samiksha.constants.Constants;
import in.amigoscorp.samiksha.models.Review;

/**
 * Created by sriny on 08/02/17.
 */

public class AwsUtils extends AsyncTask<String, Void, Void> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TAG = AwsUtils.class.getSimpleName();

    @Override
    protected Void doInBackground(String... params) {
        String ACCESS_KEY = "AKIAIWIN6MZXRUZNXBJQ";
        String SECRET_KEY = "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";
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 amazonS3 = new AmazonS3Client(awsCredentials);
        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        try {
            S3ObjectInputStream reviewsInputStream = amazonS3.getObject(new GetObjectRequest("samiksha", "reviews.json")).getObjectContent();
            List<String> reviewsLines = IOUtils.readLines(reviewsInputStream, Charset.forName("UTF-8"));
            String rawReviews = StringUtils.join(reviewsLines, " ");
            Constants.reviews = objectMapper.readValue(rawReviews, new TypeReference<List<Review>>() {
            });
            reviewsInputStream.close();
            S3ObjectInputStream upcomingInputStream = amazonS3.getObject(new GetObjectRequest("samiksha", "upcoming.json")).getObjectContent();
            List<String> upcomingLines = IOUtils.readLines(upcomingInputStream, Charset.forName("UTF-8"));
            String rawUpcoming = StringUtils.join(upcomingLines, " ");
            Constants.reviews = objectMapper.readValue(rawUpcoming, new TypeReference<List<Review>>() {
            });
            reviewsInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
