package com.ruoyi.web.controller.kdd;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kdd/taskphoto")
public class TaskwebPageController {

    private String prefix = "kdd/taskphoto";


    @RequiresPermissions("kdd:taskphoto:view")
    @GetMapping()
    public String taskphoto() {
        return prefix + "/taskphoto";
    }


}
