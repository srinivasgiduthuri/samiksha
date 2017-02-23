package in.amigoscorp.samiksha.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sriny on 23/02/17.
 */
@JsonPropertyOrder({
        "androidId",
        "firebaseToken"
})
public class User {
    @JsonProperty("androidId")
    private String androidId;
    @JsonProperty("firebaseToken")
    private String firebaseToken;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("androidId")
    public String getAndroidId() {
        return androidId;
    }

    @JsonProperty("androidId")
    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    @JsonProperty("firebaseToken")
    public String getFirebaseToken() {
        return firebaseToken;
    }

    @JsonProperty("firebaseToken")
    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
