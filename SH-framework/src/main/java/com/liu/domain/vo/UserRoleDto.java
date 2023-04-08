package com.liu.domain.vo;

import com.liu.domain.entity.Role;
import com.liu.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    private List<Role>roles;
    private User user;
    private List<Long>roleIds;
}