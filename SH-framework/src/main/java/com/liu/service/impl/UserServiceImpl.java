package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Role;
import com.liu.domain.entity.User;
import com.liu.domain.entity.UserRole;
import com.liu.domain.vo.PageVo;
import com.liu.domain.vo.UserInfoVo;
import com.liu.domain.vo.UserRoleDto;
import com.liu.domain.vo.UserVo;
import com.liu.enums.AppHttpCodeEnum;
import com.liu.exception.SystemException;
import com.liu.mapper.UserMapper;
import com.liu.service.RoleService;
import com.liu.service.UserRoleService;
import com.liu.service.UserService;
import com.liu.utils.BeanCopyUtil;
import com.liu.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserMapper userMapper;
    @Override
    public ResponseResult userInfo() {
        Long userId= SecurityUtils.getUserId();
        User user=getById(userId);
        UserInfoVo userInfoVo= BeanCopyUtil.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUser(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult registerUser(User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        System.out.println(user);
        //userMapper.insert(user);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) throws InstantiationException, IllegalAccessException {
        Page<User> page=new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.hasText(userName))
        {
            lambdaQueryWrapper.like(User::getUserName,userName);
        }
        if (StringUtils.hasText(phonenumber))
        {
            lambdaQueryWrapper.like(User::getPhonenumber,phonenumber);
        }
        if (StringUtils.hasText(status))
        {
            lambdaQueryWrapper.eq(User::getStatus,status);
        }
        page(page,lambdaQueryWrapper);
        List<UserVo> userVo=BeanCopyUtil.copyBeanList(page.getRecords(),UserVo.class);
        PageVo pageVo=new PageVo(userVo,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(User user) {

        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);
        if (user.getRoleIds()!=null)
        {
          addRoleUser(user);
        }
        return ResponseResult.okResult();
    }
    @Override
    public ResponseResult getUserById(Long id) {
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole>userRoles=userRoleService.list(queryWrapper);
        List<Long>RoleId=userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        List<Role>roleList=RoleId.stream()
                .map(roleId->roleService.getById(roleId))
                .collect(Collectors.toList());
        User user=getById(id);
        UserRoleDto userRoleDto=new UserRoleDto(roleList,user,RoleId);
        return ResponseResult.okResult(userRoleDto);
    }

    private void addRoleUser(User user){
    List<UserRole> userRoles=Arrays.stream(user.getRoleIds())
            .map(RoleId->new UserRole(user.getId(),RoleId))
            .collect(Collectors.toList());
    userRoleService.saveBatch(userRoles);
}
    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.eq(User::getNickName,nickName);
        return count(lambdaQueryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        return count(lambdaQueryWrapper)>0;
    }
}
