package com.ruoyi.web.controller.kdd;

import com.ruoyi.kdd.service.LottteryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kdd/lottery")
public class LotteryController {

    private String prefix = "kdd/lottery";

    @Autowired
    private LottteryService lottteryService;

    @RequiresPermissions("kdd:lottery:view")
    @GetMapping()
    public String lottery() {
        return prefix + "/lottery";
    }

    //https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?gameNo=85&provinceId=0&pageSize=30&isVerify=1&pageNo=1
}
