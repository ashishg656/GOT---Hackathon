package com.ashishgoel.got.sqlite.kings;

/**
 * Created by Ashish on 12/09/16.
 */
public class KingsSqliteObject {

    private String title;
    int id;
    private String detailObject;

    public String getDetailObject() {
        return detailObject;
    }

    public void setDetailObject(String detailObject) {
        this.detailObject = detailObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
