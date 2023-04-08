package com.liu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {
    //文章id
    private Long id;
    //文章标题
    private String title;
    //访问量
    private Long viewCount;
}
