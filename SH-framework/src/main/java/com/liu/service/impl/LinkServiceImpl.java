package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.constants.SystemConstants;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Link;
import com.liu.domain.vo.LinkVo;
import com.liu.domain.vo.PageVo;
import com.liu.mapper.LinkMapper;
import com.liu.service.LinkService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper,Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Link> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_NORMAL);
        List<Link>linkList=list(lambdaQueryWrapper);
        List<LinkVo>linkVoList= BeanCopyUtil.copyBeanList(linkList,LinkVo.class);
        return ResponseResult.okResult(linkVoList);
    }

    @Override
    public PageVo getLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            lambdaQueryWrapper.like(Link::getName, name);
        }
        if (StringUtils.hasText(status)) {
            lambdaQueryWrapper.like(Link::getStatus, status);
        }
        Page<Link> page=new Page(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        return new PageVo(page.getRecords(),page.getTotal());
    }
}
