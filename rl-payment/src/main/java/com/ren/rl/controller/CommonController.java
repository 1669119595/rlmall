package com.ren.rl.controller;

import com.ren.rl.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping("/getResponseString")
    public String getResponse(){
        return "success";
    }

    @GetMapping("/doRetry")
    public String doRetry(){
        commonService.doWorkWithRetry();
        return "success";
    }

}
