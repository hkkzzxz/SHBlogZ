package com.liu.controller;

import com.liu.constants.SystemConstants;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Comment;
import com.liu.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController{
@Autowired
private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize) throws InstantiationException, IllegalAccessException {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment)
    {
        return commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize) throws InstantiationException, IllegalAccessException {
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
