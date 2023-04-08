package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Menu;
import com.liu.domain.entity.Role;
import com.liu.domain.entity.RoleMenu;
import com.liu.domain.vo.MenuVo;
import com.liu.domain.vo.PageVo;
import com.liu.domain.vo.RoleVo;
import com.liu.mapper.RoleMapper;
import com.liu.service.RoleMenuService;
import com.liu.service.RoleService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (id==1L)
        {
            List<String> roleList=new ArrayList<>();
            roleList.add("admin");
            return roleList;
        }

        return roleMapper.getRoleLIst(id);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Role> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName))
        {
            lambdaQueryWrapper.like(Role::getRoleName,roleName);
        }
        if (StringUtils.hasText(status))
        {
            lambdaQueryWrapper.eq(Role::getStatus,status);
        }
        Page<Role>page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<RoleVo>roleVos= BeanCopyUtil.copyBeanList(page.getRecords(),RoleVo.class);
        PageVo pageVo=new PageVo(roleVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRole(Role role) {
        return ResponseResult.okResult(roleMapper.updateById(role));
    }

    @Override
    @Transactional
    public ResponseResult addRole(Role role) {
        save(role);
        if (role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
        return ResponseResult.okResult();
    }
    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.
                stream(role.getMenuIds())
                .map(menuId ->new RoleMenu(role.getId(),menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}
