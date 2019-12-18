package com.hnhy.ylfz.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by guc on 2019/12/10.
 * 描述：论坛
 */
public class Forum implements Parcelable {
    public String id;//论坛ID
    public String title;//标题
    public String date;//发布日期
    public String content;//内容
    public String picUrl;//图片Url
    public List<ForumReply> replyList;//论坛回复

    public static final Parcelable.Creator<Forum> CREATOR = new Parcelable.Creator<Forum>() {
        @Override
        public Forum createFromParcel(Parcel source) {
            return new Forum(source);
        }

        @Override
        public Forum[] newArray(int size) {
            return new Forum[size];
        }
    };

    public Forum() {
    }

    protected Forum(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.content = in.readString();
        this.picUrl = in.readString();
        this.replyList = in.createTypedArrayList(ForumReply.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.content);
        dest.writeString(this.picUrl);
        dest.writeTypedList(this.replyList);
    }
}
