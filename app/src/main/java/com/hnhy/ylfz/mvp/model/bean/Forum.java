package com.hnhy.ylfz.mvp.model.bean;

import java.util.List;

/**
 * Created by guc on 2019/12/10.
 * 描述：论坛
 */
public class Forum {
    public String id;//论坛ID
    public String title;//标题
    public String date;//发布日期
    public String content;//内容
    public String picUrl;//图片Url
    public List<ForumReply> replyList;//论坛回复
}
