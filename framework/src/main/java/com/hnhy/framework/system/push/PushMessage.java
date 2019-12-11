package com.hnhy.framework.system.push;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guc on 2018/10/25.
 * 描述：
 */
public class PushMessage implements Parcelable {
    public static final Parcelable.Creator<PushMessage> CREATOR = new Parcelable.Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel source) {
            return new PushMessage(source);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };
    /**
     * id : 通知列表中id
     * sourceId : 通知详情信息id
     * noticeType : 2
     * noticeTypeDesc : 通知类型描述
     * title : 标题
     * content : 内容
     * status : 预警签收状态
     * createTime : 创建时间
     * sfry : 涉访人员
     * ①　noticeType:通知类型（1：公告， 4：稳控状态变更， 7：下发通知， 11：红色预警， 12：橙色预警， 13：黄色预警， 14：蓝色预警， 15：群体预警）
     * ②　status:预警签收状态只针对预警通知（1：待签收， 2：待处置， 3：已完成）
     * ③　id:通知列表中该通知id （非预警通知id返回通知列表id，预警通知返回该预警id）
     * ④　sourceId:通知详情信息id（非预警通知返回通知详情的id，预警通知返回该预警id）
     */

    public String id;//通知列表中id
    public String sourceId;//通知详情信息id
    public int noticeType;//通知类型
    public String noticeTypeDesc;// 通知类型描述
    public String title;//标题
    public String titleNew;//人员变更时标题
    public String content;//内容
    public String status;// 预警签收状态
    public String createTime;//创建时间
    public String sfry;//涉访人员

    public PushMessage() {
    }

    protected PushMessage(Parcel in) {
        this.id = in.readString();
        this.sourceId = in.readString();
        this.noticeType = in.readInt();
        this.noticeTypeDesc = in.readString();
        this.title = in.readString();
        this.titleNew = in.readString();
        this.content = in.readString();
        this.status = in.readString();
        this.createTime = in.readString();
        this.sfry = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.sourceId);
        dest.writeInt(this.noticeType);
        dest.writeString(this.noticeTypeDesc);
        dest.writeString(this.title);
        dest.writeString(this.titleNew);
        dest.writeString(this.content);
        dest.writeString(this.status);
        dest.writeString(this.createTime);
        dest.writeString(this.sfry);
    }
}
