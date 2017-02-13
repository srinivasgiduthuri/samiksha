package in.amigoscorp.samiksha.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by sriny on 09/02/17.
 */

@JsonPropertyOrder({
        "name",
        "language",
        "rank"
})
public class Tab {

    @JsonProperty("name")
    private String name;
    @JsonProperty("language")
    private String language;
    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
