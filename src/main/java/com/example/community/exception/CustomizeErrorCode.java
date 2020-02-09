package com.example.community.exception;

public enum CustomizeErrorCode implements iCustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "你似乎来到了没有知识的荒原"),
    TARGET_PARAM_NOT_FOUND(2002, "未选择任何评论或回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登陆后再试"),
    PLEASE_FLUSH(3001, "服务器出了点问题，刷新一下试试吧"),
    SYSTEM_ERROR(2004, "服务器冒烟了，请带盆水来(╯‵□′)╯︵┻━┻"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "你找的评论被淹没在人海了"),
    CONTENT_IS_EMPTY(2007, "评论得有点东西"),
    NOT_YOUR_NOTIFICATION(2008, "这条通知不是给你的"),
    NOTIFICATION_NOT_FOUND(2009, "这条通知不见了"),
    FILE_UPLOAD_FAILED(2010, "图片上传失败"),

    ;

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


}
