package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Comment;

public interface CommentService extends IService<Comment> {
    ResponseResult commentList(String articleComment, Long articleId, Integer pageNum, Integer pageSize) throws InstantiationException, IllegalAccessException;

    ResponseResult addComment(Comment comment);
}
