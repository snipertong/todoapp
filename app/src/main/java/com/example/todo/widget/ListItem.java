package com.example.todo.widget;

/**
 * Created by kapil on 27/9/17.
 */

public class ListItem {
    private String title;
    private String content;
    private String date;
    private long created;
    private long accessed;
    private long id;
    private byte[] photo;

    public ListItem(String title, String content, long id,String date, long epoch1, long epoch2) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.id = id;
//        this.photo=photo;
        this.created = epoch1;
        this.accessed = epoch2;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated() {
        return created;
    }

    public long getAccessed() {
        return accessed;
    }

    public void setAccessed(long accessed) {
        this.accessed = accessed;
    }

//    public byte[] getTvatar() {
//        return photo;
//    }
//
//    public void setTvatar(byte[] photo) {
//        this.photo = photo;
//    }


}
