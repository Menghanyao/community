package com.example.community.service;

import com.example.community.dto.CommentDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.CommentMapper;
import com.example.community.mapper.NotificationMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Comment;
import com.example.community.model.Notification;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //  回复评论
            System.out.println("回复评论");
            Comment dbComment = commentMapper.selectById(comment.getParentId());    //  根据parentId找出对应的父评论
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.updateCommentCount(dbComment, dbComment.getCommentCount() + 1);
            commentMapper.insert(comment);

            //  添加一条通知
            createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);

        } else {
            //  回复问题
            System.out.println("回复问题");
            Question dbQuestion = questionMapper.getById(comment.getParentId());
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incCommentSQL(dbQuestion, dbQuestion.getCommentCount() + 1);

            //  添加一条通知
            createNotify(comment, dbQuestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION);
        }
    }


    //  添加一条通知
      private void createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationType) {
        if (receiver == comment.getCommentator()) {

            return;
        }
        Notification notification = new Notification();
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setOuterId(comment.getParentId());
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setGmtCreate(comment.getGmtCreate());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listCommentsByParentIdAndType(Long parentId, Integer type) {
        List<CommentDTO> commentDTOs = new ArrayList<>();

        //  找评论(根据parentId和type)
        List<Comment> dbComments = commentMapper.selectByParentIdAndType(parentId, type);

        if (dbComments.size() != 0) {
            //  根据每条评论的commentator寻找评论者
            for (Comment dbComment : dbComments) {
                User dbUser = userMapper.findById(dbComment.getCommentator());  //找出人
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(dbComment, commentDTO);
                commentDTO.setUser(dbUser);     //  为commentDTO赋予 评论+评论人
                commentDTOs.add(commentDTO);    //  放进列表里
            }
        }
        System.out.println("get评论 commentDTOs = " + commentDTOs);
        return commentDTOs;
    }
}
