package com.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.domain.entity.ArticleTag;
import com.liu.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    List<Long>taglist(Long id);
}
