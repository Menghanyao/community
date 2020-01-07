package com.example.community.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long parentId;
    private String content;
    private Integer type;
    private Long gmtCreate;
    private Long gmtModified;
    private Long commentator;
    private Long likeCount;
    private Long commentCount;
}
