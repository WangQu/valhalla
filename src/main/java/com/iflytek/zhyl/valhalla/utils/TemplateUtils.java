package com.iflytek.zhyl.valhalla.utils;

import com.raqsoft.report.model.ReportDefine;
import com.raqsoft.report.usermodel.DataSetMetaData;
import com.raqsoft.report.util.ReportUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author quwang2
 */
public class TemplateUtils {

    public static List<String> getDsNames(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = FileUtils.cloneInputStream(inputStream);
        InputStream temp = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ReportDefine rd = (ReportDefine) ReportUtils.read(temp);
        temp.close();
        inputStream.close();
        DataSetMetaData dataSetMetaData = rd.getDataSetMetaData();
        List<String> dataSourceNameList = new ArrayList<>();
        for (int i = 0; i < dataSetMetaData.getDataSetConfigCount(); i++) {
            dataSourceNameList.add(dataSetMetaData.getDataSetConfig(0).getDataSourceName());
        }
        return dataSourceNameList;
    }
}
