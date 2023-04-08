package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.constants.SystemConstants;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Menu;
import com.liu.mapper.MenuMapper;
import com.liu.service.MenuService;
import com.liu.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Lazy
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService {
    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (id==1L)
        {
            LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            lambdaQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu>menus=list(lambdaQueryWrapper);
            List<String>perms=menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        MenuMapper menuMapper = getBaseMapper();
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status))
        {
            lambdaQueryWrapper.eq(Menu::getStatus,status);
        }
        if (StringUtils.hasText(menuName))
        {
            lambdaQueryWrapper.like(Menu::getMenuName,menuName);
        }
        return ResponseResult.okResult(list(lambdaQueryWrapper));
    }
    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,menuId);
        return count(lambdaQueryWrapper)>0;
    }
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        return menus;
    }
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
