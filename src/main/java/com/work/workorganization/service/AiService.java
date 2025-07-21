package com.work.workorganization.service;

import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.request.AiRequest;

/**
 * -@Desc: BigModelAI大模型服务层
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/18 17:55
 **/
public interface AiService {

    /**
     * AI智能解答
     */
    ResponseBo testAI(AiRequest aiRequest);

}
