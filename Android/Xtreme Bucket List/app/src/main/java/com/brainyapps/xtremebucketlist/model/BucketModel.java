package com.brainyapps.xtremebucketlist.model;

import com.brainyapps.xtremebucketlist.listener.ObjectListener;
import com.brainyapps.xtremebucketlist.utility.BaseTask;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;

//@ParseClassName("BucketList")
//public class BucketModel extends ParseObject {
public class BucketModel {
//    public static String FIELD_OWNER        = "owner";
//    public static String FIELD_GOAL         = "goal";
//    public static String FIELD_DETAIL       = "detail";
//    public static String FIELD_NOTE         = "note";
//    public static String FIELD_PICTURE      = "picture";
//    public static String FIELD_IS_COMPLETED = "isCompleted";
//    public static String FIELD_LINK         = "link";

    public String goal;
    public String detail;
    public String note;
    public String link;
    public String pictureFilePath;
    public boolean isCompleted;

    public BucketModel(){
        goal ="";
        detail = "";
        note = "";
        link = "";
        pictureFilePath = "";
        isCompleted = false;
    }

    public String toJsonString(){
        JSONArray jsonArray = new JSONArray();

        jsonArray.put(goal);
        jsonArray.put(detail);
        jsonArray.put(note);
        jsonArray.put(link);
        jsonArray.put(pictureFilePath);
        jsonArray.put(isCompleted);

        return jsonArray.toString();
    }
    public static BucketModel getBucketModelFromJsonStr(String strJson){
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            if (jsonArray.length() >= 6) {
                BucketModel bucketModel = new BucketModel();
                bucketModel.goal = jsonArray.getString(0);
                bucketModel.detail = jsonArray.getString(1);
                bucketModel.note = jsonArray.getString(2);
                bucketModel.link = jsonArray.getString(3);
                bucketModel.pictureFilePath = jsonArray.getString(4);
                bucketModel.isCompleted = jsonArray.getBoolean(5);

                return bucketModel;
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
//    public UserModel getOwner() { return (UserModel)getParseUser(FIELD_OWNER); }
//    public String getGoal() {return getString(FIELD_GOAL);}
//    public String getDetail() {return getString(FIELD_DETAIL);}
//    public String getNote() {return getString(FIELD_NOTE);}
//    public String getLink() {return getString(FIELD_LINK);}
//    public ParseFile getPicture() {return getParseFile(FIELD_PICTURE);}
//    public String getPictureUrl() {
//        ParseFile file =  getParseFile(FIELD_PICTURE);
//        if (file != null){
//            return file.getUrl();
//        } else {
//            return null;
//        }
//    }
//    public boolean getIsCompleted() {return getBoolean(FIELD_IS_COMPLETED);}
//
//    public void setOwner(UserModel value) { put(FIELD_OWNER, value); }
//    public void setGoal(String value) {put(FIELD_GOAL, value);}
//    public void setDetail(String value) {put(FIELD_DETAIL, value);}
//    public void setNote(String value) {put(FIELD_NOTE, value);}
//    public void setLink(String value) {put(FIELD_LINK, value);}
//    public void setPicture(ParseFile value) {put(FIELD_PICTURE, value);}
//    public void setIsCompleted(boolean value) {put(FIELD_IS_COMPLETED, value);}
//
//    public static void Register(final BucketModel model, final ObjectListener listener) {
//        BaseTask.run(new BaseTask.TaskListener() {
//
//            @Override
//            public Object onTaskRunning(int taskId, Object data) {
//                try {
//                    if (model.getPicture() != null)
//                        model.getPicture().save();
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//                return null;
//            }
//            @Override
//            public void onTaskResult(int taskId, Object result) {
//                // TODO Auto-generated method stub
//
//                model.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            if (listener != null)
//                                listener.done(model, null);
//                        } else {
//                            if (listener != null)
//                                listener.done(model, ParseErrorHandler.handle(e));
//                        }
//                    }
//                });
//            }
//            @Override
//            public void onTaskProgress(int taskId, Object... values) {}
//            @Override
//            public void onTaskPrepare(int taskId, Object data) {}
//            @Override
//            public void onTaskCancelled(int taskId) {}
//        });
//    }
}
