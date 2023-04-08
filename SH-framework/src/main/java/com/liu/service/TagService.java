package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.TagListDto;
import com.liu.domain.entity.Tag;
import com.liu.domain.vo.PageVo;
import com.liu.domain.vo.TagVo;

public interface TagService extends IService<Tag> {
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult insertTag(TagListDto tagListDto);

    ResponseResult updateTag(TagVo tagVo);

    ResponseResult getTagList() throws InstantiationException, IllegalAccessException;
}
