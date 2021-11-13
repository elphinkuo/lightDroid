package com.elphin.framework.app.fpstack;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * HistoryRecord about page
 *
 * @version 1.0
 * @author  elphin
 * @date 13-6-8 6:43 pm
 */
public class HistoryRecord implements Parcelable {

    public HistoryRecord(String task,String page) {
        this.taskName = task;
        this.pageName = page;
        this.taskSignature = "";
        this.pageSignature = "";
    }

    public HistoryRecord(Parcel in) {
        this.taskName = in.readString();
        this.taskSignature = in.readString();
        this.pageName = in.readString();
        this.pageSignature = in.readString();
    }

    public String taskName;
    public String pageName;

    /**
     * Task 标识，使用task的hashCode
     */
    public String taskSignature;

    /**
     * page 标识，使用page的tag
     */
    public String pageSignature;

    @Override
    public String toString() {
       return String.format("%s%s|%s%s", taskName,taskSignature, pageName,pageSignature);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        HistoryRecord record = (HistoryRecord)obj;
        boolean sameTask = taskName.equals(record.taskName) && taskSignature.equals(record.taskSignature);
        boolean samePage  = false;
        if(record.pageName != null && this.pageName != null)
            samePage = pageName.equals(record.pageName) && pageSignature.equals(record.pageSignature);
        if(record.pageName == null && this.pageName == null)
            samePage = false;
        return sameTask && samePage;

    }

    public boolean equalsIgnoreSig(Object obj) {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        HistoryRecord record = (HistoryRecord)obj;
        boolean sameTask = taskName.equals(record.taskName);
        boolean samePage  = false;
        if(record.pageName != null && this.pageName != null)
            samePage = pageName.equals(record.pageName);
        if(record.pageName == null && this.pageName == null)
            samePage = false;
        return sameTask && samePage;
    }

    public static String genSignature(Object obj) {
        return "@" + Integer.toHexString(obj.hashCode());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskName);
        dest.writeString(this.taskSignature);
        dest.writeString(this.pageName);
        dest.writeString(this.pageSignature);
    }

    public static final Parcelable.Creator<HistoryRecord> CREATOR
            = new Parcelable.Creator<HistoryRecord>() {
        public HistoryRecord createFromParcel(Parcel in) {
            return new HistoryRecord(in);
        }

        public HistoryRecord[] newArray(int size) {
            return new HistoryRecord[size];
        }
    };
}
