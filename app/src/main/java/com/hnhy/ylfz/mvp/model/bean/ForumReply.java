package com.hnhy.ylfz.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guc on 2019/12/10.
 * 描述：绩效论坛回复
 */
public class ForumReply implements Parcelable {
    public String id;//论坛id
    public String replyId;//回复者id
    public String replyer;//回复者姓名
    public String content;//回复内容
    public String picUrl;//回复这头像url

    public static final Parcelable.Creator<ForumReply> CREATOR = new Parcelable.Creator<ForumReply>() {
        @Override
        public ForumReply createFromParcel(Parcel source) {
            return new ForumReply(source);
        }

        @Override
        public ForumReply[] newArray(int size) {
            return new ForumReply[size];
        }
    };

    public ForumReply() {
    }

    protected ForumReply(Parcel in) {
        this.id = in.readString();
        this.replyId = in.readString();
        this.replyer = in.readString();
        this.content = in.readString();
        this.picUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.replyId);
        dest.writeString(this.replyer);
        dest.writeString(this.content);
        dest.writeString(this.picUrl);
    }
}
