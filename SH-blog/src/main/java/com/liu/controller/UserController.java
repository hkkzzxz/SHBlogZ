package com.liu.controller;

import com.liu.annotion.SystemLog;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.User;
import com.liu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){

        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(buinessName="更新用户信息")
    public ResponseResult updateUser(@RequestBody User user)
    {
        return userService.updateUser(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        System.out.println("okkdowk"+user);
        return userService.registerUser(user);
    }
}
