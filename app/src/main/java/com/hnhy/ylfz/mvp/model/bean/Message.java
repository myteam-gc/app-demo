package com.hnhy.ylfz.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.hnhy.ylfz.app.Constant;

/**
 * Created by guc on 2019/12/17.
 * 描述：消息
 */
public class Message implements Parcelable {
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    public String date;//日期
    public String title;//标题
    public String content;//内容
    public String picUrl = Constant.DEFAULT_PICTURE;//图片url
    public int type;//0:认证申请 1：指标预警

    public Message() {
    }

    protected Message(Parcel in) {
        this.date = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.picUrl = in.readString();
        this.type = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.picUrl);
        dest.writeInt(this.type);
    }
}
