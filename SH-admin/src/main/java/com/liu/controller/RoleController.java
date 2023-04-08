package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Role;
import com.liu.domain.vo.ChangeRoleStatusDto;
import com.liu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum,Integer pageSize,String roleName,String status) throws InstantiationException, IllegalAccessException {
          return  roleService.getRoleList(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeRole(@RequestBody ChangeRoleStatusDto roleStatusDto){
         Role role=new Role();
         role.setId(roleStatusDto.getRoleId());
         role.setStatus(roleStatusDto.getStatus());
         return ResponseResult.okResult(roleService.updateById(role));
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody Role role){
         return roleService.addRole(role);
    }
    @PutMapping
    public ResponseResult edit(@RequestBody Role role){
        return roleService.updateRole(role);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(name = "id")Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }
    @GetMapping("/listAllRole")
    public ResponseResult getRoleList(){
        return ResponseResult.okResult(roleService.list());
    }
}
