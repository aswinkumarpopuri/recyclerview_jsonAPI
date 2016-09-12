
package co.brighterbrain.glide.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private Url url;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The url
     */
    public Url getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(Url url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
