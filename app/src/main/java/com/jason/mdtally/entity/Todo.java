package com.jason.mdtally.entity;


import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.UUID;

public class Todo extends DataSupport {
    private String mId;
    private boolean checked;
    private String content;
    private Date Date;

    public Todo() {
        super();
        mId = UUID.randomUUID().toString();
    }

    public Todo(boolean checked, String content) {
        this.checked = checked;
        this.content = content;
    }

    public Todo(String mId, boolean checked, String content, java.util.Date date) {
        this.mId = mId;
        this.checked = checked;
        this.content = content;
        Date = date;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "mId='" + mId + '\'' +
                ", checked=" + checked +
                ", content='" + content + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return Date;
    }

    public Todo(boolean checked, String content, java.util.Date date) {
        this.checked = checked;
        this.content = content;
        Date = date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }
}
