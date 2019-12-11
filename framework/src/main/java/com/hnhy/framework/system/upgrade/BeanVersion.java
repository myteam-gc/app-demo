package com.hnhy.framework.system.upgrade;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wjy
 * Date: 2018/7/26
 * Description:
 */
public class BeanVersion implements Parcelable {

    public static final Creator<BeanVersion> CREATOR = new Creator<BeanVersion>() {
        @Override
        public BeanVersion createFromParcel(Parcel source) {
            return new BeanVersion(source);
        }

        @Override
        public BeanVersion[] newArray(int size) {
            return new BeanVersion[size];
        }
    };
    public boolean needUpdate;
    public boolean forceUpdate;
    public String fileUrl;
    public String fileMd5;
    public String fileSize;
    public String newVersion;
    public String updateJournal;
    public String mSaveFileDir;
    public String mSaveFileName;

    protected BeanVersion(Parcel in) {
        this.needUpdate = in.readByte() != 0;
        this.forceUpdate = in.readByte() != 0;
        this.fileUrl = in.readString();
        this.fileMd5 = in.readString();
        this.fileSize = in.readString();
        this.newVersion = in.readString();
        this.updateJournal = in.readString();
        this.mSaveFileDir = in.readString();
        this.mSaveFileName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.needUpdate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.forceUpdate ? (byte) 1 : (byte) 0);
        dest.writeString(this.fileUrl);
        dest.writeString(this.fileMd5);
        dest.writeString(this.fileSize);
        dest.writeString(this.newVersion);
        dest.writeString(this.updateJournal);
        dest.writeString(this.mSaveFileDir);
        dest.writeString(this.mSaveFileName);
    }
}
