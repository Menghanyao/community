package com.example.community.mapper;

import com.example.community.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) " +
            "values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id = #{id}")
    Comment selectById(@Param(value = "id") Long id);

    @Select("select * from comment where parent_id = #{parentId} and type = #{type} order by gmt_create desc")
    List<Comment> selectByParentIdAndType(@Param(value = "parentId") Long parentId, @Param(value = "type") Integer type);

    @Update("update comment set comment_count = #{newCommentCount} where id = #{comment.id}")
    void updateCommentCount(Comment comment, @Param(value = "newCommentCount") long newCommentCount);

    @Update("update comment set like_count = #{likeCount} where id = #{parentId}")
    void addLikeCount(@Param(value = "parentId") Long parentId, @Param(value = "likeCount")  Long likeCount);
}
