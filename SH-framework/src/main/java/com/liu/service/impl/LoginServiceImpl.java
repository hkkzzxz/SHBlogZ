package com.liu.service.impl;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.LoginUser;
import com.liu.domain.entity.User;
import com.liu.service.LoginService;
import com.liu.utils.JwtUtil;
import com.liu.utils.RedisCache;
import com.liu.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (Objects.isNull(authenticate))
        {
            throw new RuntimeException("密码或账号错误");
        }
        LoginUser loginUser=(LoginUser) authenticate.getPrincipal();
        String userId=loginUser.getUser().getId().toString();
        String jwt= JwtUtil.createJWT(userId);
        redisCache.setCacheObject("login:"+userId,loginUser);
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long UserId=SecurityUtils.getUserId();
        redisCache.deleteObject("login"+UserId);
        return ResponseResult.okResult();
    }
}
