package com.example.community.service;

import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        //  如果只有4页，你要查第5页，应返回第4页
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);    //  设置本页的分页条,主要为了设置totalPage
        if (page < 1) { page = 1; }
        if (page > paginationDTO.getTotalPage()) { page = paginationDTO.getTotalPage(); }

        paginationDTO.setPagination(totalCount, page, size);    //  设置本页的分页条,主要为了设置totalPage
        //  offset是数据库查询的偏移量
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);   //查询对应页码的帖子

        //  for循环为每条帖子赋予作者信息
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);    //  设置本页的问题

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        //  如果只有4页，你要查第5页，应返回第4页
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);    //  设置本页的分页条,主要为了设置totalPage
        if (page < 1) { page = 1; }
        if (page > paginationDTO.getTotalPage()) { page = paginationDTO.getTotalPage(); }

        paginationDTO.setPagination(totalCount, page, size);    //  设置本页的分页条,主要为了设置totalPage
        //  offset是数据库查询的偏移量
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);   //查询对应页码的帖子

        //  for循环为每条帖子赋予作者信息
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);    //  设置本页的问题
        return paginationDTO;
    }
}
