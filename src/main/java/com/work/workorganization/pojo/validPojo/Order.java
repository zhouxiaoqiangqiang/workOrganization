package com.work.workorganization.pojo.validPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

/**
  * -@Desc: 分组校验
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/24 9:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    // 定义分组标识接口 add 新增操作
    public interface Add extends Default {}
    // 定义分组标识接口 update 修改操作
    public interface Update extends Default {}
    // 定义分组标识接口 delete 删除操作
    public interface Delete extends Default {}



    @Null(groups = Add.class) // add 分组
    @NotNull(message="订单号不能为空", groups = {Update.class,Delete.class}) // update delete分组 且校验顺序为先校验空
    private Integer id;
	
	// 下单用户不能为空
	@NotBlank(message = "下单用户不能未空",groups = {Add.class,Update.class})
	private String userName;
 }
