package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.constants.SystemConstants;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Article;
import com.liu.domain.entity.Category;
import com.liu.domain.vo.Categoryvo;
import com.liu.domain.vo.PageVo;
import com.liu.mapper.CategoryMapper;
import com.liu.service.CategoryService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleServiceImpl articleService;

    public ResponseResult getCategoryList() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);
        Set<Long> Articleset = articleList.stream().
                map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        List<Category> categories = listByIds(Articleset);
        categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus())
                )
                .collect(Collectors.toList());

        List<Categoryvo> CategoryvoList = BeanCopyUtil.copyBeanList(categories, Categoryvo.class);
        return ResponseResult.okResult(CategoryvoList);
    }

    @Override
    public ResponseResult listAllCategory() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> categories = list(queryWrapper);
        List<Categoryvo> categoryvos = BeanCopyUtil.copyBeanList(categories, Categoryvo.class);
        return ResponseResult.okResult(categoryvos);
    }

    @Override
    public PageVo selectCategoryPage(Integer pageNum, Integer pageSize, String name, String status) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            lambdaQueryWrapper.like(Category::getName, name);
        }
        if (StringUtils.hasText(status)) {
            lambdaQueryWrapper.like(Category::getStatus, status);

        }
        page(page, lambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return pageVo;
    }
}
