package in.amigoscorp.samiksha.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import in.amigoscorp.samiksha.constants.Constants;
import in.amigoscorp.samiksha.models.Review;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sriny on 08/02/17.
 */

public class AwsUtils extends AsyncTask<String, Void, Void> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TAG = AwsUtils.class.getSimpleName();
    private static final OkHttpClient CLIENT = new OkHttpClient();

    @Override
    protected Void doInBackground(String... params) {
        try {
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
            /*S3ObjectInputStream tabsInputStream = amazonS3.getObject(new GetObjectRequest("samiksha", "tabs.json")).getObjectContent();
            List<String> tabsLines = IOUtils.readLines(tabsInputStream, Charset.forName("UTF-8"));
            String rawTabs = StringUtils.join(tabsLines, " ");
            Log.i(TAG, rawTabs);
            Constants.tabs = OBJECT_MAPPER.readValue(rawTabs, new TypeReference<List<Tab>>() {
            });
            tabsInputStream.close();*/
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
