package com.liu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private String content;
    private Long viewCount;
    private String isComment;
    private Long id;
    private String categoryName;
    private Date createTime;
    private String title;
    private Long categoryId;
}


