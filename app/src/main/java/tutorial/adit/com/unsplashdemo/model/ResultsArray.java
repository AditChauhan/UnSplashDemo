/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.model;

import com.google.gson.annotations.SerializedName;

public class ResultsArray {
    @SerializedName("urls") private Urls urls;
    @SerializedName("user") private User user;
    @SerializedName("id") private String id;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Urls getUrls() {
        return urls;
    }
}
