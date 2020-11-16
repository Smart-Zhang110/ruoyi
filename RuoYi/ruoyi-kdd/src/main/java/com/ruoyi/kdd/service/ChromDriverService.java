package com.ruoyi.kdd.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ChromDriverService {

    int order(Map paramters) throws Exception;

}
