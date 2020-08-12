package com.im.controller;

import com.im.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;
    @RequestMapping("/get")
    public String get(@RequestParam("key") String key){
        return  redisUtil.get(key)+"";
    }
}
