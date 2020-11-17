package com.ruoyi.kdd.service.impl;

import java.util.Map;

public class chromExecute implements Runnable {

    Map paramters;

    public void setParamters(Map paramters) {
        this.paramters = paramters;
    }

    @Override
    public synchronized void run() {
        try {
            ChromDriverServiceImpl chromDriverService = new ChromDriverServiceImpl();
            chromDriverService.order(paramters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
