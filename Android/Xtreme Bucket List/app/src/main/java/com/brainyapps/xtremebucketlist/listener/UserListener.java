package com.brainyapps.xtremebucketlist.listener;

import com.parse.ParseUser;

public interface UserListener {
    public void done(ParseUser user, String error);
}
