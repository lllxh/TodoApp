package com.jason.mdtally.entity;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Todo extends DataSupport implements Serializable {
    private String mId;
    private boolean checked;
    private String content;
    private String Date;
    private String remindTime;

    public Todo() {
    }

    public Todo(boolean checked) {
        this.checked = checked;
    }

    public Todo(String mId, boolean checked, String content, String date, String remindTime) {
        this.mId = mId;
        this.checked = checked;
        this.content = content;
        Date = date;
        this.remindTime = remindTime;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "mId='" + mId + '\'' +
                ", checked=" + checked +
                ", content='" + content + '\'' +
                ", Date='" + Date + '\'' +
                ", remindTime='" + remindTime + '\'' +
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }
}
