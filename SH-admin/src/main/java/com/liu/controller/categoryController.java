package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Category;
import com.liu.domain.vo.AlterCategoryVo;
import com.liu.domain.vo.PageVo;
import com.liu.service.CategoryService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/category")
public class categoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String name,String status) {
        PageVo pageVo = categoryService.selectCategoryPage(pageNum,pageSize,name,status);
        return ResponseResult.okResult(pageVo);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable Long id){
        Category category=categoryService.getById(id);
        AlterCategoryVo alterCategoryVo= BeanCopyUtil.copyBean(category,AlterCategoryVo.class);
        return ResponseResult.okResult(alterCategoryVo);
    }
    @PutMapping
    public ResponseResult AlterCategory(@RequestBody AlterCategoryVo categoryVo){
        Category category=BeanCopyUtil.copyBean(categoryVo,Category.class);
        return ResponseResult.okResult(categoryService.updateById(category));
    }
    @PostMapping
    public ResponseResult AddCategory(@RequestBody AlterCategoryVo categoryVo)
    {
        Category category=BeanCopyUtil.copyBean(categoryVo,Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult DeleteCategory(@PathVariable Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

}
