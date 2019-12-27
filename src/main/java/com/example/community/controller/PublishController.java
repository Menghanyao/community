package com.example.community.controller;

import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
import com.example.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
     private QuestionService questionService;

    //  展示列表
    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    //  展示具体内容（供修改）
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        //  id标签做修改时使用
        model.addAttribute("id", question.getId());
        return "publish";
    }

    //  增
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "/publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "描述不能为空");
            return "/publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "/publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);

        User user = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length != 0) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    user = userMapper.findByToken(token);
//                    if (user != null) {
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
//        }
        user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "/publish";
        }
        question.setCreator(user.getId());
        question.setViewCount(0);
        question.setCommentCount(0);
        question.setLikeCount(0);
        //  id是增或改的判断
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
