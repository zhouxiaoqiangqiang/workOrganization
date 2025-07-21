package com.work.workorganization.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  * -@Desc: BigModelAI大模型接口请求类
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/21 9:42
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRequest {

    private String question;

}
