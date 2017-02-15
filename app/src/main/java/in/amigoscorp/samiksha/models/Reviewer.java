package in.amigoscorp.samiksha.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sriny on 30/01/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "source",
        "max_rating",
        "actual_rating",
        "punch_line"
})
public class Reviewer implements Parcelable {

    @JsonProperty("source")
    private String source;
    @JsonProperty("max_rating")
    private int maxRating;
    @JsonProperty("actual_rating")
    private int actualRating;
    @JsonProperty("punch_line")
    private String punchLine;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Reviewer() {
    }

    public Reviewer(Parcel in) {
        source = in.readString();
        maxRating = in.readInt();
        actualRating = in.readInt();
        punchLine = in.readString();
    }

    public static final Creator<Reviewer> CREATOR = new Creator<Reviewer>() {
        @Override
        public Reviewer createFromParcel(Parcel in) {
            return new Reviewer(in);
        }

        @Override
        public Reviewer[] newArray(int size) {
            return new Reviewer[size];
        }
    };

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("max_rating")
    public int getMaxRating() {
        return maxRating;
    }

    @JsonProperty("max_rating")
    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    @JsonProperty("actual_rating")
    public int getActualRating() {
        return actualRating;
    }

    @JsonProperty("actual_rating")
    public void setActualRating(int actualRating) {
        this.actualRating = actualRating;
    }

    @JsonProperty("punch_line")
    public String getPunchLine() {
        return punchLine;
    }

    @JsonProperty("punch_line")
    public void setPunchLine(String punchLine) {
        this.punchLine = punchLine;
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
        parcel.writeString(source);
        parcel.writeInt(maxRating);
        parcel.writeInt(actualRating);
        parcel.writeString(punchLine);
    }
}