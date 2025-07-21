package com.work.workorganization.controller;

import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.request.AiRequest;
import com.work.workorganization.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * -@Desc:   BigModelAI大模型控制层
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/18 17:53
 **/
@Slf4j
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AiService aiService;

    /**
     * AI智能解答
     */
    @PostMapping("/testAI")
    public ResponseBo testAI(@RequestBody AiRequest aiRequest) {
        return aiService.testAI(aiRequest);
    }
}
