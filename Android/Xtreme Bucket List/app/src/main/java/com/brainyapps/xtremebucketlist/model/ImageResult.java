package com.brainyapps.xtremebucketlist.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageResult implements Serializable {
    private String fullUrl;
    private String thumbUrl;
    private String title;

    public String getFullUrl() {
        return fullUrl;
    }
    public String getThumbUrl() {
        return thumbUrl;
    }
    public String getTitle() {
        return title;
    }

    public ImageResult(JSONObject json){
        try {
            this.fullUrl = json.getString("link");
            this.title = json.getString("title");
            this.thumbUrl = json.getJSONObject("image").getString("thumbnailLink");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray (JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<>();
        for (int i=0; i < array.length(); i++){
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
/*
Sample Image search result item
    {
        "kind": "customsearch#result",
        "title": "Visit Madrid - Official Tourist Website",
        "htmlTitle": "Visit \u003cb\u003eMadrid\u003c/b\u003e - Official Tourist Website",
        "link": "https://www.esmadrid.com/sites/default/files/styles/slider_home_full/public/vistaaerea_palacioreal.jpg?itok=hcg2oxd8",
        "displayLink": "www.esmadrid.com",
        "snippet": "Visit Madrid - Official Tourist Website",
        "htmlSnippet": "Visit \u003cb\u003eMadrid\u003c/b\u003e - Official Tourist Website",
        "mime": "image/jpeg",
        "image": {
            "contextLink": "https://www.esmadrid.com/en",
            "height": 650,
            "width": 1903,
            "byteSize": 355382,
            "thumbnailLink": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpCJwJaqkJhLe5_imzzk69GeznDCO_e03aSovfDfG-EwsfEXWKFNoFOVI",
            "thumbnailHeight": 51,
            "thumbnailWidth": 150
        }
    }
*/