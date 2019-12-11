package com.hnhy.framework.system.download;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.File;

public class Task implements Parcelable {
    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    public int tag;
    public String url;
    public String saveFileDir;
    public String saveFileName;
    public String saveFilePath;
    public int status;
    public int type_callback;
    public String msg;
    public int progress;

    public Task() {
    }

    protected Task(Parcel in) {
        this.tag = in.readInt();
        this.url = in.readString();
        this.saveFileDir = in.readString();
        this.saveFileName = in.readString();
        this.saveFilePath = in.readString();
        this.status = in.readInt();
        this.type_callback = in.readInt();
        this.msg = in.readString();
        this.progress = in.readInt();
    }

    @Override
    public String toString() {
        return "Task{" +
                "tag=" + tag +
                ", url='" + url + '\'' +
                ", saveFileDir='" + saveFileDir + '\'' +
                ", saveFileName='" + saveFileName + '\'' +
                ", saveFilePath='" + saveFilePath + '\'' +
                ", status=" + status +
                ", type_callback=" + type_callback +
                ", msg='" + msg + '\'' +
                ", progress=" + progress +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tag);
        dest.writeString(this.url);
        dest.writeString(this.saveFileDir);
        dest.writeString(this.saveFileName);
        dest.writeString(this.saveFilePath);
        dest.writeInt(this.status);
        dest.writeInt(this.type_callback);
        dest.writeString(this.msg);
        dest.writeInt(this.progress);
    }

    public static class Builder {
        int t;
        String u;
        String sd;
        String sn;

        public Builder setTag(int tag) {
            this.t = tag;
            return this;
        }

        public Builder setUrl(String url) {
            this.u = url;
            return this;
        }

        public Builder setSaveFileDir(String saveFileDir) {
            this.sd = saveFileDir;
            return this;
        }

        public Builder setSaveFileName(String saveFileName) {
            this.sn = saveFileName;
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.tag = this.t;
            task.url = this.u;
            task.saveFileDir = this.sd;
            task.saveFileName = this.sn;
            if (TextUtils.isEmpty(sn)) {
                File tempFile = new File(u.trim());
                task.saveFilePath = sd + tempFile.getName();
            } else {
                task.saveFilePath = sd + sn;
            }

            return task;
        }
    }
}
