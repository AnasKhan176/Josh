package com.joshtalk.model;

import java.util.Comparator;

/**
 * Created by ravi on 16/11/17.
 */

public class PostModel {
    String id;
    String thumbnail_image;
    String event_name;

    long event_date;
    long views;
    long likes;
    long shares;

    public PostModel() {

    }

    public PostModel(String id, String thumbnail_image, String event_name, long event_date, long views, long likes, long shares) {
        this.id = id;
        this.thumbnail_image = thumbnail_image;
        this.event_name = event_name;
        this.event_date = event_date;
        this.views = views;
        this.likes = likes;
        this.shares = shares;
    }

    public String getId() {
        return id;
    }

    public long getEvent_date() {
        return event_date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public long getLikes() {
        return likes;
    }

    public long getShares() {
        return shares;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public void setEvent_date(long event_date) {
        this.event_date = event_date;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    /*Comparator for sorting the list by roll no*/
    public static Comparator<PostModel> view = new Comparator<PostModel>() {
        public int compare(PostModel s1, PostModel s2) {
            int v1 = (int) s1.getViews();
            int v2 = (int) s2.getViews();
            /*For ascending order*/
            return v1 - v2;
            /*For descending order*/
            //v2-v1;
        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<PostModel> like = new Comparator<PostModel>() {
        public int compare(PostModel s1, PostModel s2) {
            int l1 = (int) s1.getLikes();
            int l2 = (int) s2.getLikes();
            /*For ascending order*/
            return l1 - l2;
            /*For descending order*/
            //l2-l1;
        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<PostModel> share = new Comparator<PostModel>() {
        public int compare(PostModel s1, PostModel s2) {
            int ss1 = (int) s1.getShares();
            int ss2 = (int) s2.getShares();
            /*For ascending order*/
            return ss1 - ss2;
            /*For descending order*/
            //ss2-ss1;
        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<PostModel> date = new Comparator<PostModel>() {
        public int compare(PostModel s1, PostModel s2) {
            int date1 = (int) s1.getEvent_date();
            int date2 = (int) s2.getEvent_date();
            /*For ascending order*/
            return date1 - date2;
            /*For descending order*/
            //date2-date1;
        }
    };

}
