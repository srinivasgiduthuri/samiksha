package in.amigoscorp.samiksha.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import in.amigoscorp.samiksha.models.Review;
import in.amigoscorp.samiksha.models.User;

/**
 * Created by sriny on 08/02/17.
 */

public final class Constants {
    public static List<Review> reviews;
    public static List<Review> upcoming;
    public static String androidId;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static User user = new User();
}
