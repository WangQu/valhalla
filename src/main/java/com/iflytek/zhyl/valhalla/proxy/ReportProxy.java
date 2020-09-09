package com.iflytek.zhyl.valhalla.proxy;

import com.raqsoft.cellset.BackGraphConfig;
import com.raqsoft.report.usermodel.*;
import lombok.Data;

import java.util.Map;

@Data
public class ReportProxy {
    //private transient IReport report;
    private int rowCount;
    private int colCount;
    private DataSetMetaData dataSetMetaData;
    private ParamMetaData paramMetaData;
    private ParamMetaData staticParamMetaData;
    private ParamMetaData dynamicParamMetaData;
    private BackGraphConfig backGraphConfig;
    private String bigDataSetName;
    private String chartStyleName;
    private Map<?,?> customProperties;
    private int displayRatio;
    private MacroMetaData macroMetaData;
    private String futureCellNumExp;
    private PrintSetup printSetup;
    private String password;
    private String notes;
    private String pagerListener;
    private String tip;
    private byte pageBorderMode;
    private byte unit;
    private String validityScript;
    private byte reportType;

    public ReportProxy(IReport iReport){
        this.setReport(iReport);
    }

    public void setReport(IReport iReport){
        System.out.println(iReport.getClass());
        this.rowCount = iReport.getRowCount();
        this.colCount = iReport.getColCount();
        this.dataSetMetaData = iReport.getDataSetMetaData();
        this.paramMetaData = iReport.getParamMetaData();
        this.backGraphConfig = iReport.getBackGraphConfig();
        this.staticParamMetaData = iReport.getStaticParamMetaData();
        this.macroMetaData = iReport.getDynamicMacroMetaData();
        this.bigDataSetName = iReport.getBigDataSetName();
        this.chartStyleName = iReport.getChartStyleName();
        this.customProperties = iReport.getCustomProperties();
        this.displayRatio = iReport.getDispRatio();
        this.futureCellNumExp = iReport.getFutureCellNumExp();
        this.printSetup = iReport.getPrintSetup();
        this.password = iReport.getPassword();
        this.notes = iReport.getNotes();
        this.pagerListener = iReport.getPagerListener();
        this.tip = iReport.getTip();
        this.pageBorderMode = iReport.getPageBorderMode();
        this.unit = iReport.getUnit();
        this.validityScript = iReport.getValidityScript();
        this.reportType = iReport.getReportType();
        this.dynamicParamMetaData = iReport.getDynamicParamMetaData();
    }

}
