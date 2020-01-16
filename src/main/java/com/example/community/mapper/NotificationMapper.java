package com.example.community.mapper;

import com.example.community.model.Notification;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification (notifier,receiver,outer_id,type,gmt_create,status) " +
            "values (#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status})")
    void insert(Notification notification);

    @Select("select count(1) from notification where receiver = #{receiver}")
    Integer countByReceiver(@Param(value = "receiver") Long receiver);

    @Select("select * from notification where receiver = #{userId} order by status, gmt_create desc limit #{offset},#{size}")
    List<Notification> listByReceiver(@Param(value = "userId") Long userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select * from user where id = #{notifier}")
    User findNotifier(@Param(value = "notifier") Long notifier);

    @Select("select count(1) from notification where receiver = #{receiver} and status = 0")
    int getUnreadCountByReceiver(@Param(value = "receiver") Long receiver);

    @Select("select * from notification where id = #{id}")
    Notification getRecordById(@Param(value = "id") Long id);

    @Update("update notification set status = #{status} where id = #{id}")
    void setStatusById(@Param(value = "id") Long id,@Param(value = "status")  int status);
}
