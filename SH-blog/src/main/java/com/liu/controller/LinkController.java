package com.liu.controller;

import com.liu.domain.ResponseResult;
import com.liu.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink() throws InstantiationException, IllegalAccessException {
        return linkService.getAllLink();
    }
}
