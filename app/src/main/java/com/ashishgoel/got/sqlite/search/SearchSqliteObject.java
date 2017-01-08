package com.ashishgoel.got.sqlite.search;

/**
 * Created by Ashish on 12/09/16.
 */
public class SearchSqliteObject {

    private String searchText;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
