package com.example.community.service;

import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            //  insert
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //  updata
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            dbUser.setGmtCreate(System.currentTimeMillis());
            userMapper.update(dbUser);
        }
    }
}
