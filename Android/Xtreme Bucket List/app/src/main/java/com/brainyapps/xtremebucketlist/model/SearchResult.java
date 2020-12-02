package com.brainyapps.xtremebucketlist.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable {
    private String link;
    private String snippet;
    private String title;

    public String getLink() {
        return link;
    }
    public String getSnippet() {
        return snippet;
    }
    public String getTitle() {
        return title;
    }

    public SearchResult(JSONObject json){
        try {
            this.link = json.getString("link");
            this.title = json.getString("title");
            this.snippet = json.getString("snippet");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<SearchResult> fromJSONArray (JSONArray array){
        ArrayList<SearchResult> results = new ArrayList<>();
        for (int i=0; i < array.length(); i++){
            try {
                results.add(new SearchResult(array.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}

/*
Search Result item sample
{
   "kind": "customsearch#result",
   "title": "Madrid - Wikipedia",
   "htmlTitle": "\u003cb\u003eMadrid\u003c/b\u003e - Wikipedia",
   "link": "https://en.wikipedia.org/wiki/Madrid",
   "displayLink": "en.wikipedia.org",
   "snippet": "Madrid is the capital of Spain and the largest municipality in both the Community \nof Madrid and Spain as a whole. The city has almost 3.2 million inhabitants and ...",
   "htmlSnippet": "\u003cb\u003eMadrid\u003c/b\u003e is the capital of Spain and the largest municipality in both the Community \u003cbr\u003e\nof \u003cb\u003eMadrid\u003c/b\u003e and Spain as a whole. The city has almost 3.2 million inhabitants and&nbsp;...",
   "cacheId": "EFo23MjMSqYJ",
   "formattedUrl": "https://en.wikipedia.org/wiki/Madrid",
   "htmlFormattedUrl": "https://en.wikipedia.org/wiki/\u003cb\u003eMadrid\u003c/b\u003e",
   "pagemap": {
    "cse_thumbnail": [
     {
      "width": "275",
      "height": "183",
      "src": "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRfk0hiWa3rqHBOYb38QrC5b2vxte2fWybVl6vdCHNObRtNCljzxSNgSzI"
     }
    ],
    "metatags": [
     {
      "referrer": "origin",
      "og:image": "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Calle_de_Alcal%C3%A1_%28Madrid%29_16.jpg/1200px-Calle_de_Alcal%C3%A1_%28Madrid%29_16.jpg"
     }
    ],
    "hcard": [
     {
      "fn": "Madrid",
      "category": "Capital of Spain"
     }
    ],
    "cse_image": [
     {
      "src": "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Calle_de_Alcal%C3%A1_%28Madrid%29_16.jpg/1200px-Calle_de_Alcal%C3%A1_%28Madrid%29_16.jpg"
     }
    ]
   }
  }
 */