package com.example.community.mapper;

import com.example.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question " +
            "(title,description,gmt_create,gmt_modified,creator,tag,view_count,comment_count,like_count) " +
            "values " +
            "(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag},#{viewCount},#{commentCount},#{likeCount})")
    void create(Question question);

    @Select("select * from question order by gmt_create desc limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") Long userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId") Long userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") Long id);

    @Update("update question set title = #{title}, description = #{description}, tag = #{tag}, gmt_modified = #{gmtModified} where id = #{id}")
    int update(Question question);

    @Update("update question set view_count = #{newViewCount} where id = #{updateQuestion.id}")
    int incViewSQL(Question updateQuestion, @Param(value = "newViewCount") int newViewCount);

    @Update("update question set comment_count = #{newCommentCount} where id = #{updateQuestion.id}")
    int incCommentSQL(Question updateQuestion, @Param(value = "newCommentCount") int newCommentCount);

    @Select("select * from question where tag regexp #{tag} and id != #{id}")
    List<Question> listByTag(@Param(value = "tag") String tag, @Param(value = "id") Long id);

    @Select("select count(1) from question where title regexp #{search}")
    Integer searchCount(@Param(value = "search") String search);

    @Select("select * from question where title regexp #{search} limit #{offset},#{size}")
    List<Question> searchList(@Param(value = "search") String search, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);
}
