package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
//    @GetMapping("list")
//    public List<Article> test(){
//        return articleService.list();
//    }
    @GetMapping("/hotArticleList")
    public ResponseResult hostArticleList() throws InstantiationException, IllegalAccessException {
        ResponseResult result=articleService.hotArticleList();
        System.out.println("ye"+result);
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId) throws InstantiationException, IllegalAccessException {
        return articleService.articleList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult ArticleDetails(@PathVariable Integer id)
    {
        System.out.println("来来来");
        return  articleService.articleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult UpdateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }

}
