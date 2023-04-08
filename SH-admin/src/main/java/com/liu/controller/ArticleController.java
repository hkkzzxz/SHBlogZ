package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Article;
import com.liu.domain.vo.AddArticleDto;
import com.liu.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }
    @GetMapping("/list")
    public ResponseResult getArticleList(Integer pageNum,Integer pageSize,String title,String summary) throws InstantiationException, IllegalAccessException {
     return articleService.getArticleList(pageNum,pageSize,title,summary);
    }
    @GetMapping("/{id}")
    public ResponseResult GetArticleById(@PathVariable Long id){
     return articleService.getArticleDetail(id);
    }
    @PutMapping
    public ResponseResult AlterArticle(@RequestBody AddArticleDto addArticleDto){
       return articleService.alterArticle(addArticleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult DeleteArticle(@PathVariable Long id){
        return ResponseResult.okResult(articleService.removeById(id));
    }
}
