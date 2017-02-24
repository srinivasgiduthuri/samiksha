package in.amigoscorp.samiksha.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sriny on 30/01/17.
 */

@JsonPropertyOrder({
        "id",
        "name",
        "image_url",
        "language",
        "reviewers",
        "rank",
        "positives",
        "negatives",
        "rating",
        "description"
})
public class Review implements Parcelable {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("language")
    private String language;
    @JsonProperty("trailer_id")
    private String trailerId;
    @JsonProperty("reviewers")
    private List<Reviewer> reviewers;
    @JsonProperty("rank")
    private Integer rank;
    @JsonProperty("positives")
    private List<String> positives;
    @JsonProperty("negatives")
    private List<String> negatives;
    @JsonProperty("rating")
    private float rating;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Review() {
    }

    protected Review(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageUrl = in.readString();
        language = in.readString();
        trailerId = in.readString();
        rank = in.readInt();
        reviewers = in.createTypedArrayList(Reviewer.CREATOR);
        positives = in.createStringArrayList();
        negatives = in.createStringArrayList();
        rating = in.readFloat();
        description = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("trailer_id")
    public String getTrailerId() {
        return trailerId;
    }

    @JsonProperty("trailer_id")
    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    @JsonProperty("reviewers")
    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    @JsonProperty("reviewers")
    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @JsonProperty("positives")
    public List<String> getPositives() {
        return positives;
    }

    @JsonProperty("positives")
    public void setPositives(List<String> positives) {
        this.positives = positives;
    }

    @JsonProperty("negatives")
    public List<String> getNegatives() {
        return negatives;
    }

    @JsonProperty("negatives")
    public void setNegatives(List<String> negatives) {
        this.negatives = negatives;
    }

    @JsonProperty("rating")
    public float getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(float rating) {
        this.rating = rating;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeString(language);
        parcel.writeString(trailerId);
        parcel.writeInt(rank);
        parcel.writeTypedList(reviewers);
        parcel.writeStringList(positives);
        parcel.writeStringList(negatives);
        parcel.writeFloat(rating);
        parcel.writeString(description);
    }
}