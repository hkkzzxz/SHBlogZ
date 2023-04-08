package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.constants.SystemConstants;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Article;
import com.liu.domain.entity.ArticleTag;
import com.liu.domain.entity.Tag;
import com.liu.domain.vo.*;
import com.liu.mapper.ArticleMapper;
import com.liu.mapper.ArticleTagMapper;
import com.liu.mapper.TagMapper;
import com.liu.service.ArticleService;
import com.liu.service.ArticleTagService;
import com.liu.service.CategoryService;
import com.liu.utils.BeanCopyUtil;
import com.liu.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    @Lazy
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Override
    public ResponseResult hotArticleList() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByAsc(Article::getViewCount);
        Page<Article>page=new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> articles=page.getRecords();
//        List<HotArticleVo> hotArticleVoList=new ArrayList<>();
//        for (Article article:articles)
//        {
//            HotArticleVo vo=new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            hotArticleVoList.add(vo);
//        }
        List<HotArticleVo> hotArticleVoList= BeanCopyUtil.copyBeanList(articles,HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVoList);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Article>articleLambdaQueryWrapper=new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        articleLambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        articleLambdaQueryWrapper.orderByAsc(Article::getIsTop);
        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page,articleLambdaQueryWrapper);
        List<ArticleListVo>articleListVos=BeanCopyUtil.copyBeanList(page.getRecords(),ArticleListVo.class);
//        for (ArticleListVo articleListVo:articleListVos)
//        {
//            Long id=articleListVo.getCategoryId();
//            Category category=categoryService.getById(id);
//            articleListVo.setCategoryName(category.getName());
//        }
        articleListVos.stream()
                .map(articleListVo -> articleListVo.setCategoryName(categoryService.getById(articleListVo.getCategoryId()).getName()))
                .collect(Collectors.toList());
        PageVo pageVo=new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult articleDetail(Integer id) {
//        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper();
        Article article=  getById(id);
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo articleDetailVo=BeanCopyUtil.copyBean(article,ArticleDetailVo.class);
        articleDetailVo.setCategoryName(categoryService.getById(articleDetailVo.getCategoryId()).getName());
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtil.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags=articleDto.getTags().stream()
                .map(tagId->new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, String title, String summary) throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (StringUtils.hasText(title))
        {
            lambdaQueryWrapper.like(Article::getTitle,title);
        }
        if (StringUtils.hasText(summary))
        {
            lambdaQueryWrapper.like(Article::getSummary,summary);
        }
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        Page<Article>page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
//        记得copy属性
        PageVo pageVo=new PageVo(BeanCopyUtil.copyBeanList(page.getRecords(),ArticleListVo.class),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getId,id);
        List<Long> tagList=articleTagMapper.taglist(id);
        AddArticleDto articleDto=BeanCopyUtil.copyBean(getOne(lambdaQueryWrapper),AddArticleDto.class);
        articleDto.setTags(tagList);
        return ResponseResult.okResult(articleDto);
    }

    @Override
    public ResponseResult alterArticle(AddArticleDto addArticleDto) {
        LambdaUpdateWrapper lambdaUpdateWrapper=new LambdaUpdateWrapper();
        Article article=BeanCopyUtil.copyBean(addArticleDto,Article.class);
        save(article);
        List<ArticleTag> articleTags=addArticleDto.getTags().stream()
                .map(tagId->new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        return ResponseResult.okResult();
    }

}
