package com.example.community.controller;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.mapper.CommentMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.model.Comment;
import com.example.community.model.Question;
import com.example.community.model.User;
import com.example.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id,
                          Model model) {

        Cookie[] cookies = request.getCookies();
        User user = (User) request.getSession().getAttribute("user");
        if (cookies == null || cookies.length == 0 || user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (notificationDTO.getNotification().getType().equals(NotificationTypeEnum.REPLY_QUESTION.getType())) {
            return "redirect:/question/"+notificationDTO.getNotification().getOuterId();
        } else {
            Comment comment = commentMapper.selectById(notificationDTO.getNotification().getOuterId());
            Question question = questionMapper.getById(comment.getParentId());
            Long questionId = question.getId();
            String questionTitle = question.getTitle();
            notificationDTO.setQuestionId(questionId);
            notificationDTO.setQuestionTitle(questionTitle);
            return "redirect:/question/"+notificationDTO.getQuestionId();
        }
    }
}
