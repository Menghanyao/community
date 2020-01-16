package com.example.community.dto;

import com.example.community.model.Notification;
import com.example.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {

//    private Long id;
//    private Long gmtCreate;
//    private Integer status;
//    private String outerTitle;
//    private String type;

    private User user;
    private Notification notification;

    private String Name;    //  问题或评论名称(类型在notification里)

    //  以下两个是选做，当回复的是评论时才找出原始问题
    private String questionTitle;
    private Long questionId;    //当回复的是评论时跳转到对应的问题下面
//    private Long notifier;
//    private Long receiver;
//    private Long outerId;
//    private Integer type;
//    private Long gmtCreate;
//    private Integer status;
}
