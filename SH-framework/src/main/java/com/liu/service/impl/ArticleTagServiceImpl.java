package com.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.domain.entity.ArticleTag;
import com.liu.mapper.ArticleTagMapper;
import com.liu.service.ArticleService;
import com.liu.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl< ArticleTagMapper,ArticleTag> implements ArticleTagService {
}
