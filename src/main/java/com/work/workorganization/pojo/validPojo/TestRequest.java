package com.work.workorganization.pojo.validPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
/**
  * -@Desc:   注解校验
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/24 9:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRequest {

    @NotBlank(message = "name不为空")
    private String name;

    @NotBlank(message = "address不为空")
    @Length(max = 3,message = "address最大长度是3")
    private String address;

    @NotBlank(message = "reqNo不为空")
    @Max(value = 5,message = "reqNo最大值是5")
    private String reqNo;

}
