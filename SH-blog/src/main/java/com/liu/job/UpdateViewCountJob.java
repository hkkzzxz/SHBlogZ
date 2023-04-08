package com.liu.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.liu.domain.entity.Article;
import com.liu.service.ArticleService;
import com.liu.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");

        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        System.out.println("javaee");
        System.out.println(articles);
        for (Article article:articles)
        {
            LambdaUpdateWrapper<Article> queryWrapper=new LambdaUpdateWrapper<>();
            queryWrapper.eq(Article::getId,article.getId());
            queryWrapper.set(Article::getViewCount,article.getViewCount());
            articleService.update(queryWrapper);
        }
        //更新到数据库中
        articleService.updateBatchById(articles);

    }
}