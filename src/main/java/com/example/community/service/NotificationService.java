package com.example.community.service;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.CommentMapper;
import com.example.community.mapper.NotificationMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.model.Comment;
import com.example.community.model.Notification;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        //  如果只有4页，你要查第5页，应返回第4页
        Integer totalCount = notificationMapper.countByReceiver(userId);
        paginationDTO.setPagination(totalCount, page, size);    //  设置本页的分页条,主要为了设置totalPage
        if (page < 1) { page = 1; }
        if (page > paginationDTO.getTotalPage()) { page = paginationDTO.getTotalPage(); }

        paginationDTO.setPagination(totalCount, page, size);    //  设置本页的分页条,主要为了设置totalPage
        //  offset是数据库查询的偏移量
        Integer offset = size * (page - 1);
        List<Notification> notifications = notificationMapper.listByReceiver(userId, offset, size);   //查询对应页码的帖子

        //  for循环为每条帖子赋予作者信息
        for (Notification notification : notifications) {
            User user = notificationMapper.findNotifier(notification.getNotifier());

            //  找一下问题或者评论的标题/内容
            String name = getQuestionOrCommentName(notification.getOuterId(), notification.getType());

            NotificationDTO notificationDTO = new NotificationDTO();

            notificationDTO.setNotification(notification);
            notificationDTO.setUser(user);
            notificationDTO.setName(name);

            if (notification.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()) {
                // 我还得知道它回复的是哪个问题的评论，不然没法跳转
                Comment comment = commentMapper.selectById(notification.getOuterId());
//                if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
//                } else {}
                Question question = questionMapper.getById(comment.getParentId());
                Long questionId = question.getId();
                String questionTitle = question.getTitle();
                notificationDTO.setQuestionId(questionId);
                notificationDTO.setQuestionTitle(questionTitle);
            }

            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setDataList(notificationDTOList);    //  设置本页的问题
        return paginationDTO;
    }

    private String getQuestionOrCommentName(Long questionOrCommentId, Integer type) {
        // 1是问题，2是评论
        if (type.equals(NotificationTypeEnum.REPLY_QUESTION.getType())) {
            Question question = questionMapper.getById(questionOrCommentId);
            String questionTitle = question.getTitle();
            return questionTitle;
        } else if (type.equals(NotificationTypeEnum.REPLY_COMMENT.getType())) {
            Comment comment = commentMapper.selectById(questionOrCommentId);
            String commentContent = comment.getContent();
            return  commentContent;
        }
        return null;
    }

    public int getUnreadCountByReceiver(Long receiver) {
        int unreadCount = notificationMapper.getUnreadCountByReceiver(receiver);
        return unreadCount;
    }

    @Transactional
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.getRecordById(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.NOT_YOUR_NOTIFICATION);
        }
        NotificationDTO notificationDTO = new NotificationDTO();
        notification.setType(1);
        notificationMapper.setStatusById(id, NotificationStatusEnum.READ.getStatus());
        notificationDTO.setNotification(notification);
        notificationDTO.setUser(user);
        return notificationDTO;
    }
}
