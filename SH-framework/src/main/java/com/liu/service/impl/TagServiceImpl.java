package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.domain.ResponseResult;
import com.liu.domain.TagListDto;
import com.liu.domain.entity.Tag;
import com.liu.domain.vo.PageVo;
import com.liu.domain.vo.TagVo;
import com.liu.enums.AppHttpCodeEnum;
import com.liu.exception.SystemException;
import com.liu.mapper.TagMapper;
import com.liu.service.TagService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag>page=new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult insertTag(TagListDto tagListDto) {
        if (!StringUtils.hasText(tagListDto.getName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (tagnameExist(tagListDto.getName()))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        Tag tag=new Tag();
        tag.setName(tagListDto.getName());
        if (StringUtils.hasText(tagListDto.getRemark()))
        {
            tag.setRemark(tagListDto.getRemark());
        }

        return ResponseResult.okResult(save(tag));
    }

    @Override
    public ResponseResult updateTag(TagVo tagVo) {
        LambdaUpdateWrapper<Tag> updateWrapper=new LambdaUpdateWrapper<>();
        Tag tag= BeanCopyUtil.copyBean(tagVo,Tag.class);
        return ResponseResult.okResult(updateById(tag));

    }

    @Override
    public ResponseResult getTagList() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> tags=list(queryWrapper);
        List<TagVo>tagVos=BeanCopyUtil.copyBeanList(tags,TagVo.class);
        return ResponseResult.okResult(tagVos);
    }

    private boolean tagnameExist(String name){
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName,name);
        return count(queryWrapper)>0;
    }
}
