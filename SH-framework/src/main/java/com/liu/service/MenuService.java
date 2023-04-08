package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Menu;
import com.liu.domain.entity.User;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    ResponseResult getMenuList(String status, String menuName);
    boolean hasChild(Long menuId);
    List<Menu> selectMenuList(Menu menu);
}
