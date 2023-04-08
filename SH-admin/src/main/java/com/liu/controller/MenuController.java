package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Menu;
import com.liu.domain.vo.MenuTreeVo;
import com.liu.domain.vo.MenuVo;
import com.liu.service.MenuService;
import com.liu.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult getMenuList(String status,String menuName){
        return menuService.getMenuList(status,menuName);
    }
    @PostMapping
    public ResponseResult AddMenu(@RequestBody Menu menu){
        return ResponseResult.okResult(menuService.save(menu));
    }
    @GetMapping("{id}")
    public ResponseResult getMenu(@PathVariable Long id){
        return ResponseResult.okResult(menuService.getById(id));
    }
    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChild(menuId)) {
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        List<Menu> menus = menuService.selectMenuList(new Menu());
       List<MenuTreeVo> options = SystemConverter.buildMenuSelectTree(menus);
        return  ResponseResult.okResult(options);
    }
}