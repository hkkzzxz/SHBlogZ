package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.User;
import com.liu.enums.AppHttpCodeEnum;
import com.liu.exception.SystemException;
import com.liu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult UserList(Integer pageNum, Integer pageSize,
    String userName,String phonenumber,String status) throws InstantiationException, IllegalAccessException {
        System.out.println("运行了");
    return userService.getUserList(pageNum,pageSize,userName,phonenumber,status );
    }
    @PostMapping
    public ResponseResult AddUser(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
       return userService.addUser(user);
    }
    @DeleteMapping("/{id}")
    public ResponseResult DeleteUser(@PathVariable Long id){
        return ResponseResult.okResult(userService.removeById(id));
    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable Long id)
    {
        return userService.getUserById(id);
    }
    @PutMapping
    public ResponseResult AlterUser(@RequestBody User user)
    {
        return ResponseResult.okResult(userService.updateById(user));
    }
}