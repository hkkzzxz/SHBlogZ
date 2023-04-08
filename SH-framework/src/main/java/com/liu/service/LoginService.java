package com.liu.service;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
