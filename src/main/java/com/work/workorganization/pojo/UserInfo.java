package com.work.workorganization.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @ExcelProperty(value = "工号", index = 0)
    private String userCode;//工号

    @ExcelProperty(value = "姓名", index = 1)
    private String userName;//姓名

    @ExcelProperty(value = "性别", index = 2)
    private String sex;//性别

    @ExcelProperty(value = "年龄", index = 3)
    private Integer age;//年龄

    @ExcelProperty(value = "部门名称", index = 4)
    private String department;//部门名称

    @ExcelIgnore
    private Integer departmentId;//部门id

    @ExcelProperty(value = "兴趣爱好", index = 5)
    private String interest;//兴趣爱好

    @ExcelProperty(value = "数据日期", index = 6)
    private String dataDate;//数据日期
}
