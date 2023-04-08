package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/category")
public class CateGoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryname() throws InstantiationException, IllegalAccessException {

        return categoryService.getCategoryList();
    }
}
