package com.ruoyi.web.controller.kdd;

import com.ruoyi.kdd.service.ChromDriverService;
import com.ruoyi.kdd.service.LottteryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/kdd/order")
public class TMOrderController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String prefix = "kdd/order";

    @Autowired
    private ChromDriverService chromDriverService;


    @RequiresPermissions("kdd:order:view")
    @GetMapping()
    public String lottery() {
        return prefix + "/order";
    }


    @RequestMapping("/maotai")
    @ResponseBody
    public Map OrderTM(HttpServletRequest request) throws Exception {
        Map paramters = new HashMap();
        paramters.put("date","2020-11-16 19:59:59:9");
        chromDriverService.order(paramters);
        return null;
    }

}
