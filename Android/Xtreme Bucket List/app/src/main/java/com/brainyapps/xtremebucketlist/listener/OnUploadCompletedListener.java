package com.brainyapps.xtremebucketlist.listener;

import com.parse.ParseException;
import com.parse.ParseFile;

public interface OnUploadCompletedListener {
    void onFileUploadFinished(ParseFile file, ParseException e);
}
