package com.joshtalk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.joshtalk.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class JoshTalkDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    // database name
    public static final String DATABASE_NAME = "JoshTalk.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_POSTS_DATA_ENTRIES = "CREATE TABLE "
            + "PostsDataTable" + " (" + "id"
            + TEXT_TYPE + COMMA_SEP
            + "thumbnail_image" + TEXT_TYPE + COMMA_SEP
            + "event_date" + TEXT_TYPE + COMMA_SEP
            + "event_name" + " TEXT_TYPE "+ " PRIMARY KEY"+ COMMA_SEP
            + "views" + TEXT_TYPE + COMMA_SEP
            + "likes" + TEXT_TYPE + COMMA_SEP
            + "shares" + TEXT_TYPE +

            " )";

    public JoshTalkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        try {

            // Table creation
            db.execSQL(SQL_CREATE_POSTS_DATA_ENTRIES);

        } catch (Exception e) {
            Log.e("errorrrrrrrrrrrrrrrr", e.getMessage() + "###" + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // Insert data in PostsData table
    public boolean postsData(List<PostModel> contactList) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVal = new ContentValues();

        try {
            db.beginTransaction();
            for (PostModel i : contactList) {
                cVal.put("id", i.getId());
                cVal.put("thumbnail_image", i.getThumbnail_image());
                cVal.put("event_date", String.valueOf(i.getEvent_date()));
                cVal.put("event_name", i.getEvent_name());
                cVal.put("views", String.valueOf(i.getViews()));
                cVal.put("likes", String.valueOf(i.getLikes()));
                cVal.put("shares", String.valueOf(i.getShares()));
                db.insert("PostsDataTable", null, cVal);
                Log.e("insert Posts Data", cVal.toString());
                status = true;
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("errrrrrrrrrrrrrrorrrrrr", e.toString());
        } finally {
            db.endTransaction();
        }

        db.close();
        return status;
    }


    // get Posts Data
    public List<PostModel> getPostsData() {
        List<PostModel> postList = new ArrayList<PostModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "PostsDataTable"
                + " ORDER BY " + "event_date";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    PostModel data = new PostModel();
                    data.setId(cursor.getString(0));
                    data.setThumbnail_image(cursor.getString(1));
                    data.setEvent_name(cursor.getString(3));

                    data.setEvent_date(new Long(cursor.getString(2)).longValue());
                    data.setViews(new Long(cursor.getString(4)).longValue());
                    data.setLikes(new Long(cursor.getString(5)).longValue());
                    data.setShares(new Long(cursor.getString(6)).longValue());

                    postList.add(data);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return postList;
    }

}
