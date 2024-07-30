package com.work.workorganization;

import com.work.workorganization.utils.WordToPDFUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * -@Desc: Word转PDF测试
 * -@Author: zhouzhiqiang
 * -@Date: 2024/7/30 16:55
 **/
@SpringBootTest(classes = WorkOrganizationApplication.class)
public class WordToPDFTest {

    /**
     * Word转PdF
     */
    @Test
    public void testWordToPdf() {
        WordToPDFUtils.docxToPDF("E:\\code\\workOrganization\\src\\main\\resources\\testFile\\测试Word转PDF.docx", "E:\\code\\workOrganization\\src\\main\\resources\\testFile\\测试Word转PDF.pdf");
    }

}
