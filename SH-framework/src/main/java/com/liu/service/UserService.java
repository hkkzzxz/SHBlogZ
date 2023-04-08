package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.User;

public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUser(User user);

    ResponseResult registerUser(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) throws InstantiationException, IllegalAccessException;

    ResponseResult addUser(User user);

    ResponseResult getUserById(Long id);
}