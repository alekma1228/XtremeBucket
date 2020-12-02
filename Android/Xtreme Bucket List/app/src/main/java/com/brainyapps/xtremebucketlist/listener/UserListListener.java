package com.brainyapps.xtremebucketlist.listener;

import com.parse.ParseUser;

import java.util.List;

public interface UserListListener {
    public void done(List<ParseUser> users, String error);
}
