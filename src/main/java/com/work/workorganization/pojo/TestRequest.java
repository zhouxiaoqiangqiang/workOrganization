package com.work.workorganization.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

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
