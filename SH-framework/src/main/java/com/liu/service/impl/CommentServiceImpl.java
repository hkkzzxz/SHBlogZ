package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.constants.SystemConstants;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Comment;
import com.liu.domain.vo.CommentVo;
import com.liu.domain.vo.PageVo;
import com.liu.enums.AppHttpCodeEnum;
import com.liu.exception.SystemException;
import com.liu.mapper.CommentMapper;
import com.liu.service.CommentService;
import com.liu.service.UserService;
import com.liu.utils.BeanCopyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class
  CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
@Autowired
private UserService userService;
    @Override
    public ResponseResult commentList(String articleComment, Long articleId, Integer pageNum, Integer pageSize) throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(articleComment),Comment::getArticleId,articleId);
        lambdaQueryWrapper.eq(Comment::getRootId,-1);
        lambdaQueryWrapper.eq(Comment::getType,articleComment);
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        Page<Comment>page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<CommentVo>commentVos= BeanCopyUtil.copyBeanList(page.getRecords(),CommentVo.class);
       for (CommentVo commentVo:commentVos)
       {
           String nickName=userService.getById(commentVo.getCreateBy()).getNickName();
           commentVo.setUsername(nickName);
           if (commentVo.getToCommentUserId()!=-1)
           {
               String toCommentUsername=userService.getById(commentVo.getToCommentUserId()).getNickName();
               commentVo.setToCommentUserName(toCommentUsername);
           }
       }
       for (CommentVo commentVo:commentVos)
       {
           List<CommentVo>children=getChildren(commentVo.getId());
           commentVo.setChildren(children);
       }
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    private List<CommentVo> toCommentVoList(List<Comment>List) throws InstantiationException, IllegalAccessException {
        List<CommentVo>commentVos=BeanCopyUtil.copyBeanList(List,CommentVo.class);
        return commentVos;
    }
    private List<CommentVo>getChildren(Long id) throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        List<Comment>comments=list(queryWrapper);
        List<CommentVo>commentVos= BeanCopyUtil.copyBeanList(comments,CommentVo.class);
        for (CommentVo commentVo:commentVos)
        {
            String nickName=userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            if (commentVo.getToCommentUserId()!=-1)
            {
                String toCommentUsername=userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUsername);
            }
        }
        return commentVos;
    }
    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent()))
        {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }
    }
