package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Category;
import com.liu.domain.vo.PageVo;

import java.util.List;

public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList() throws InstantiationException, IllegalAccessException;

    ResponseResult listAllCategory() throws InstantiationException, IllegalAccessException;

    PageVo selectCategoryPage(Integer pageNum, Integer pageSize, String name, String status);
}
