package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Article;
import com.liu.domain.vo.AddArticleDto;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList() throws InstantiationException, IllegalAccessException;

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) throws InstantiationException, IllegalAccessException;

    ResponseResult articleDetail(Integer id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, String title, String summary) throws InstantiationException, IllegalAccessException;

    ResponseResult getArticleDetail(Long id);

    ResponseResult alterArticle(AddArticleDto addArticleDto);
}
