package com.brainyapps.xtremebucketlist.model;

import android.graphics.Bitmap;

import com.brainyapps.xtremebucketlist.listener.ExceptionListener;
import com.brainyapps.xtremebucketlist.listener.UserListListener;
import com.brainyapps.xtremebucketlist.listener.UserListener;
import com.brainyapps.xtremebucketlist.utility.BaseTask;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.List;

@ParseClassName("_User")
public class UserModel extends ParseUser {
    public static final int TYPE_USER = 100;
    public static final int TYPE_ADMIN = 300;
    public static final int AVATAR_SIZE = 128;
    //    public static final int THUMBNAIL_SIZE = 256;
//    public static final int THUMBNAIL_SIZE = 512;
    public static final int THUMBNAIL_SIZE = 700;

    public final static String FIELD_USER_TYPE = "userType";
    public final static String FIELD_EMAIL = "email";
    public final static String FIELD_FIRST_NAME = "firstName";
    public final static String FIELD_LAST_NAME = "lastName";
    public final static String FIELD_PASSWORD = "password";
    public final static String FIELD_PREVIEW_PASSWORD = "previewPassword";
    public final static String FIELD_FACEBOOKID = "facebookid";
    public final static String FIELD_GOOOGLEID = "googleid";
    public final static String FIELD_AVATAR = "avatar";
    public final static String FIELD_FINGER_PHOTO = "fingerPhoto";
    public final static String FIELD_IS_PAID = "isPaid";
    public final static String FIELD_IS_BANNED = "isBanned";

    public Bitmap avatar = null;
    public Bitmap fingerPhoto = null;
    public UserModel() {
        super();
    }

    public int getUserType() { return getInt(FIELD_USER_TYPE); }
    public String getEmail() { return getString(FIELD_EMAIL); }
    public String getFirstName() { return getString(FIELD_FIRST_NAME); }
    public String getLastName() { return getString(FIELD_LAST_NAME); }
    public String getName() {return  getFirstName() + " " + getLastName(); }
    public String getPassword() { return getString(FIELD_PASSWORD); }
    public String getPreviewPassword() { return getString(FIELD_PREVIEW_PASSWORD); }
    public String getFacebookId() { return getString(FIELD_FACEBOOKID); }
    public String getGoogleId() { return getString(FIELD_GOOOGLEID); }
    public String getAvatarUrl() {
        ParseFile file = getParseFile(FIELD_AVATAR);
        if (file != null)
            return file.getUrl();
        else
            return null;
    }
    public ParseFile getAvatar() { return getParseFile(FIELD_AVATAR); }
    public String getFingerPhotoUrl() {
        ParseFile file = getParseFile(FIELD_FINGER_PHOTO);
        if (file != null)
            return file.getUrl();
        else
            return null;
    }
    public ParseFile getFingerPhoto() { return getParseFile(FIELD_FINGER_PHOTO); }
    public boolean getIsPaid() {return getBoolean( FIELD_IS_PAID);}
    public boolean getIsBanned() {return getBoolean( FIELD_IS_BANNED);}
    // Setter
    public void setUserType(int value) { put(FIELD_USER_TYPE, value); }
    public void setEmail(String value) { put(FIELD_EMAIL, value); }
    public void setFirstName(String value) { put(FIELD_FIRST_NAME, value); }
    public void setLastName(String value) { put(FIELD_LAST_NAME, value); }
    public void setPassword(String value) {
        put(FIELD_PASSWORD, value);
        setPreviewPassword(value);
    }
    public void setPreviewPassword(String value) { put(FIELD_PREVIEW_PASSWORD, value); }
    public void setFacebookid(String value) { put(FIELD_FACEBOOKID, value); }
    public void setGoogleId(String value) { put(FIELD_GOOOGLEID, value); }
    public void setAvatar(ParseFile file) { put(FIELD_AVATAR, file); }
    public void setFingerPhoto(ParseFile file) { put(FIELD_FINGER_PHOTO, file); }
    public void setIsPaid(boolean value) { put(FIELD_IS_PAID, value); }
    public void setIsBanned(boolean value) { put(FIELD_IS_BANNED, value); }

    // reset password
    public static void RequestPasswordReset(String strEmail, final ExceptionListener listener) {
        ParseUser.requestPasswordResetInBackground(strEmail, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (listener != null)
                    listener.done(e == null ? null : e.getMessage());
            }
        });
    }

    // register
    public static void Register(final UserModel model, final UserListener listener) {
        BaseTask.run(new BaseTask.TaskListener() {

            @Override
            public Object onTaskRunning(int taskId, Object data) {
                // TODO Auto-generated method stub
                try {
                    if (model.getAvatar() != null)
                        model.getAvatar().save();
                    if (model.getFingerPhoto() != null)
                        model.getFingerPhoto().save();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onTaskResult(int taskId, Object result) {
                // TODO Auto-generated method stub

                model.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        // TODO Auto-generated method stub
                        if (e == null) {
                            Login(model.getUsername(), model.getPreviewPassword(), new UserListener() {
                                @Override
                                public void done(ParseUser userObj, String error) {
                                    // TODO Auto-generated method stub
                                    if (listener != null)
                                        listener.done(userObj, error);
                                }
                            });
                        } else {
                            if (listener != null)
                                listener.done(model, ParseErrorHandler.handle(e));
                        }
                    }
                });
            }
            @Override
            public void onTaskProgress(int taskId, Object... values) {}
            @Override
            public void onTaskPrepare(int taskId, Object data) {}
            @Override
            public void onTaskCancelled(int taskId) {}
        });
    }

    // register
    public static void FacebookRegister(final UserModel userObj, final UserModel model, final UserListener listener) {
        BaseTask.run(new BaseTask.TaskListener() {

            @Override
            public Object onTaskRunning(int taskId, Object data) {
                // TODO Auto-generated method stub
                try {
                    if (userObj.getAvatar() != null)
                        userObj.getAvatar().save();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onTaskResult(int taskId, Object result) {
                // TODO Auto-generated method stub
                userObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Login(userObj.getEmail(), userObj.getPreviewPassword(), new UserListener() {
                                @Override
                                public void done(ParseUser userObj, String error) {
                                    // TODO Auto-generated method stub
                                    if (listener != null)
                                        listener.done(userObj, error);
                                }
                            });
                        } else {
                            if (listener != null)
                                listener.done(userObj, ParseErrorHandler.handle(e));
                        }
                    }
                });
            }
            @Override
            public void onTaskProgress(int taskId, Object... values) {}
            @Override
            public void onTaskPrepare(int taskId, Object data) {}
            @Override
            public void onTaskCancelled(int taskId) {}
        });
    }

    // login
    public static void Login(final String username, final String password, final UserListener listener) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(final ParseUser userObj, ParseException e) {
                if (e == null) { // login success
                    // for push notification
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put(ParseConstants.KEY_OWNER, userObj);
                    installation.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // TODO Auto-generated method stub
                            userObj.put(UserModel.FIELD_PREVIEW_PASSWORD, password);
                            userObj.saveInBackground();
                            if (listener != null)
                                listener.done(userObj, ParseErrorHandler.handle(e));
                        }
                    });
                } else {
                    if (listener != null)
                        listener.done(null, ParseErrorHandler.handle(e));
                }
            }
        });
    }

    // log out
    public static void Logout(final ExceptionListener listener) {
        // remove parse installation
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.remove(ParseConstants.KEY_OWNER);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException arg0) {
                // TODO Auto-generated method stub
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        // TODO Auto-generated method stub
                        if (e == null) {
                            if (listener != null)
                                listener.done(null);
                        } else {
                            if (listener != null)
                                listener.done(ParseErrorHandler.handle(e));
                        }
                    }
                });
            }
        });
    }

    public static void GetUserFromEmail(final String email, final UserListener listener) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo(UserModel.FIELD_EMAIL, email);
        userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // TODO Auto-generated method stub
                if (listener != null)
                    listener.done(user, ParseErrorHandler.handle(e));
            }
        });
    }

    public static void GetUserList(final UserListListener listener) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);
        userQuery.whereEqualTo(UserModel.FIELD_USER_TYPE, UserModel.TYPE_USER);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                // TODO Auto-generated method stub
                if (listener != null)
                    listener.done(users, ParseErrorHandler.handle(e));
            }
        });
    }

    public static void GetBannedUserList(final UserListListener listener) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);
        userQuery.whereEqualTo(UserModel.FIELD_IS_BANNED, true);
        userQuery.whereNotEqualTo(UserModel.FIELD_USER_TYPE, UserModel.TYPE_ADMIN);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                // TODO Auto-generated method stub
                if (listener != null)
                    listener.done(users, ParseErrorHandler.handle(e));
            }
        });
    }

    public static void GetUnBannedUserList(final UserListListener listener) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);
        userQuery.whereEqualTo(UserModel.FIELD_IS_BANNED, false);
        userQuery.whereNotEqualTo(UserModel.FIELD_USER_TYPE, UserModel.TYPE_ADMIN);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                // TODO Auto-generated method stub
                if (listener != null)
                    listener.done(users, ParseErrorHandler.handle(e));
            }
        });
    }
    public static void resetBanned(ParseUser user, Boolean isBanned, final ExceptionListener listener) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(UserModel.FIELD_EMAIL, user.getUsername().toString());
        params.put(UserModel.FIELD_IS_BANNED, isBanned);
        ParseCloud.callFunctionInBackground("resetBanned", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                // handle callback
                if (listener != null)
                    listener.done(ParseErrorHandler.handle(e));
            }
        });
    }
}
