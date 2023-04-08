package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Role;
import com.liu.domain.vo.MenuVo;
import com.liu.domain.vo.RoleVo;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);
    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) throws InstantiationException, IllegalAccessException;
    ResponseResult updateRole(Role role);

    ResponseResult addRole(Role role);
}
