package com.work.workorganization.service.impl;

import com.alibaba.fastjson.JSON;
import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.request.AiRequest;
import com.work.workorganization.service.AiService;
import com.work.workorganization.utils.AssertUtil;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * -@Desc:    BigModelAI大模型服务层实现类
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/18 17:55
 **/
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Resource
    private ClientV4 clientV4;

    /**
     * AI智能解答
     */
    @Override
    public ResponseBo testAI(AiRequest aiRequest) {
        List<ChatMessage> messages = this.createChatMessage(aiRequest);
        String requestId = UUID.randomUUID().toString().replace("-", "");
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4VPlus)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        return ResponseBo.ok(this.getAIResult(invokeModelApiResp));

    }

    /**
     * 校验参数并创建AI问题消息
     */
    public List<ChatMessage> createChatMessage(AiRequest aiRequest) {
        AssertUtil.isNull(aiRequest, "AI解答失败!参数不能为空!");
        AssertUtil.isBlank(aiRequest.getQuestion(), "AI解答失败!参数不能为空!");
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), aiRequest.getQuestion()));
        AssertUtil.isEmpty(messages, "抱歉，我无法回答这个问题。");
        return messages;
    }

    /**
     * 获取AI回答
     */
    public String getAIResult(ModelApiResponse modelApiResponse) {
        AssertUtil.isNull(modelApiResponse, "获取AI解答失败!调用失败!");
        log.info("Ai Response: {}", JSON.toJSONString(modelApiResponse));
        ModelData data = modelApiResponse.getData();
        AssertUtil.isNull(data, "获取AI解答失败!调用返回为空!");
        List<Choice> choices = data.getChoices();
        AssertUtil.isEmpty(choices, "获取AI解答失败!无法解答该问题!");
        ChatMessage message = choices.get(0).getMessage();
        AssertUtil.isNull(message, "获取AI解答失败!解答异常!");
        Object content = message.getContent();
        AssertUtil.isNull(content, "获取AI解答失败!无法解答该问题!");
        String result = String.valueOf(content);
        AssertUtil.isNull(result, "获取AI解答失败!无法解答该问题!");
        log.info("Ai Result: {}", result);
        return result;

    }
}
