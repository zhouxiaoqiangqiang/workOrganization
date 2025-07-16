package com.work.workorganization.controller;

import com.work.workorganization.service.EasyExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
  * -@Desc:  EasyExcel导出 controller控制层
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/16 15:15
 **/
@Slf4j
@RestController
@RequestMapping("/excel")
public class EasyExcelController {

    @Resource
    private EasyExcelService easyExcelService;

    /**
     * 导出用户信息Excel
     */
    @PostMapping("/exportUserInfoExcel")
    public void exportUserInfoExcel(HttpServletResponse httpServletResponse) {
        easyExcelService.exportUserInfoExcel(httpServletResponse);
    }

    /**
     * 导出用户信息Excel 【合并单元格】
     */
    @PostMapping("/exportUserInfoExcelMerge")
    public void exportUserInfoExcelMerge(HttpServletResponse httpServletResponse) {
        easyExcelService.exportUserInfoExcelMerge(httpServletResponse);
    }


    /**
     * 下载导入用户模板Excel
     */
    @PostMapping("/downloadUserInfoTemplate")
    public void downloadUserInfoTemplate(HttpServletResponse httpServletResponse) {
        easyExcelService.downloadUserInfoTemplate(httpServletResponse);
    }
}
