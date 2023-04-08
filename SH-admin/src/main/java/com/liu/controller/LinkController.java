package com.liu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.domain.ResponseResult;
import com.liu.domain.entity.Category;
import com.liu.domain.entity.Link;
import com.liu.domain.vo.LinkDto;
import com.liu.domain.vo.PageVo;
import com.liu.service.LinkService;
import com.liu.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize,String name,String status){
        PageVo pageVo=linkService.getLinkList(pageNum,pageSize,name,status);
        System.out.println(pageVo.getRows());
        return ResponseResult.okResult(pageVo);
    }
    @PostMapping
    public ResponseResult AddLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable Long id){
        Link link=linkService.getById(id);
        LinkDto linkDto= BeanCopyUtil.copyBean(link,LinkDto.class);
        return ResponseResult.okResult(linkDto);
    }
    @PutMapping
    public ResponseResult AlterLink(@RequestBody LinkDto linkDto){
        Link link=BeanCopyUtil.copyBean(linkDto,Link.class);
        linkService.updateById(link);
      return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult DeleteLink(@PathVariable Long id)
    {
        return ResponseResult.okResult(linkService.removeById(id));
    }
}
