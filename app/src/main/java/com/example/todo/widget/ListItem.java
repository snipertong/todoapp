package com.example.todo.widget;

/**
 * Created by kapil on 27/9/17.
 */

public class ListItem {
    public static final int FINISH=103;
    public static final int UNFINISH=104;
    private String title;
    private String content;
    private String date,end;
    private int status;
    private long id;
    private byte[] photo;

    public ListItem(String title, String content, long id, String date, String end, byte[] photo ) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.id = id;
        this.photo=photo;
        this.status=status;
        this.end = end;


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


    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
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


    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




}
