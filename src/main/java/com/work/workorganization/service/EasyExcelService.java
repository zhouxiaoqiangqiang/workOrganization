package com.work.workorganization.service;

import javax.servlet.http.HttpServletResponse;

/**
 * -@Desc:   EasyExcel导出 服务接口
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/16 15:15
 **/
public interface EasyExcelService {

    /**
     * 导出用户信息Excel
     */
    void exportUserInfoExcel(HttpServletResponse httpServletResponse);

    /**
     * 导出用户信息Excel 【合并单元格】
     */
    void exportUserInfoExcelMerge(HttpServletResponse httpServletResponse);

    /**
     * 下载导入用户模板Excel
     */
    void downloadUserInfoTemplate(HttpServletResponse httpServletResponse);
}
