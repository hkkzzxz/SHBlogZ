package com.liu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlterCategoryVo {
    private Long id;
    //分类名
    private String name;
    //父分类id，如果没有父分类为-1
    private String description;
    //状态0:正常,1禁用
    private String status;
}
