package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.LoginUser;
import com.liu.domain.entity.Menu;
import com.liu.domain.entity.User;
import com.liu.domain.vo.AdminUserInfoVo;
import com.liu.domain.vo.RoutersVo;
import com.liu.domain.vo.UserInfoVo;
import com.liu.enums.AppHttpCodeEnum;
import com.liu.exception.SystemException;
import com.liu.service.LoginService;
import com.liu.service.MenuService;
import com.liu.service.RoleService;
import com.liu.utils.BeanCopyUtil;
import com.liu.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        LoginUser loginUser= SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        User user = loginUser.getUser();
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(user, UserInfoVo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("getRouters")
    public ResponseResult getRouter(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}