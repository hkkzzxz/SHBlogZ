package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.domain.TagListDto;
import com.liu.domain.vo.PageVo;
import com.liu.domain.vo.TagVo;
import com.liu.service.TagService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult insertTag(@RequestBody TagListDto tagListDto)
    {
        return tagService.insertTag(tagListDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id)
    {
        return ResponseResult.okResult(tagService.removeById(id));
    }
    @GetMapping("/{id}")
    public ResponseResult SelectTagById(@PathVariable long id)
    {
        return ResponseResult.okResult(BeanCopyUtil.copyBean(tagService.getById(id),TagVo.class));
    }
    @PutMapping()
    public ResponseResult UpdateTag(@RequestBody TagVo tagVo)
    {
       return tagService.updateTag(tagVo);
    }
    @GetMapping("/listAllTag")
    public ResponseResult getAllTag() throws InstantiationException, IllegalAccessException {
        return tagService.getTagList();
    }
}
