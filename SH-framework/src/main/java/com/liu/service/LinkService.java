package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Link;
import com.liu.domain.vo.PageVo;

public interface LinkService extends IService<Link> {
    ResponseResult getAllLink() throws InstantiationException, IllegalAccessException;

    PageVo getLinkList(Integer pageNum, Integer pageSize, String name, String status);
}
