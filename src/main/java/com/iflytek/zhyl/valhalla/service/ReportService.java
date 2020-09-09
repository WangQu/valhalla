package com.iflytek.zhyl.valhalla.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.medicalboot.core.exception.MedicalBusinessException;
import com.iflytek.zhyl.valhalla.pojo.*;
import com.iflytek.zhyl.valhalla.utils.Msg;
import com.iflytek.zhyl.valhalla.utils.ResultUtils;
import com.iflytek.zhyl.valhalla.utils.UUIDUtils;
import com.raqsoft.report.ide.base.DataSource;
import com.raqsoft.report.model.ReportDefine;
import com.raqsoft.report.usermodel.*;
import com.raqsoft.report.util.ReportUtils;
import com.raqsoft.report.view.html.HtmlReport;
import io.swagger.annotations.*;
import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Service
@Api(tags = "报表服务")
public class ReportService {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 获取模板定义
     * @param version 版本
     * @param id 模板ID
     * @return 模板定义
     */
    @SneakyThrows
    @ApiOperation(value="获取模板定义", notes="获取模板定义")
    @GetMapping("/{version}/pt/define/{id}")
    @ApiImplicitParams({@ApiImplicitParam(name = "version",value = "版本",defaultValue = "v1",paramType = "path",required = true),
                        @ApiImplicitParam(name = "id",value = "模板编号",paramType = "path",required = true)})
    public ReportDefineProxy getTemplateDefine(@PathVariable String version, @PathVariable Long id) {
        ReportDefineProxy reportDefineProxy = getTemplate(id);
        log.info(reportDefineProxy.toString());
        return reportDefineProxy;
    }

    /**
     *获取报表HTML片段(GET)
     * @param version 版本号
     * @param id    模板ID
     * @param ps    查询参数
     * @param enc   编码
     * @param pageSet 页面设置
     * @return HTML片段
     */
    @SneakyThrows
    @ApiOperation(value="获取报表HTML片段(GET)", notes="获取报表HTML片段(GET)")
    @GetMapping("/{version}/pt/html")
    @ApiImplicitParams({@ApiImplicitParam(name = "version",type = "String",value = "版本",defaultValue = "v1",paramType = "path",required = true),
                        @ApiImplicitParam(name = "id",type = "Long",value = "模板ID",required = true),
                        @ApiImplicitParam(name = "ps",type = "String",value = "查询参数",required = false),
                        @ApiImplicitParam(name = "enc",type = "String",value = "URL编码",defaultValue = "UTF-8",required = false),
                        @ApiImplicitParam(name = "pageSet",type = "PageSet", value = "页面设置",required = false)})
    public String getHtml(@PathVariable String version, Long id, String ps, String enc, PageSet pageSet ) {
        QueryDto queryDto = new QueryDto();
        queryDto.setId(id);
        queryDto.setPageSet(pageSet);

        if(!org.springframework.util.StringUtils.isEmpty(ps) && !StringUtils.isEmpty(enc)) {
            String temp = URLDecoder.decode(ps, enc);
            Map<?, ?> parameters = objectMapper.readValue(temp, Map.class);
            queryDto.setParameters(parameters);
        }

        return getHtmlReport(queryDto).generateHtml();
    }

    /**
     * 获取报表HTML片段(POST)
     * @param version 版本号
     * @param queryDto 报表入参
     * @return 片段文本
     */
    @ApiOperation(value="获取报表HTML片段(POST)", notes="获取报表HTML片段(POST)")
    @SneakyThrows
    @PostMapping("/{version}/pt/html")
    public String getHtml(@PathVariable String version, @RequestBody QueryDto queryDto) {
       return getHtmlReport(queryDto).generateHtml();
    }

    /**
     * 导出报表
     * @param version 版本号
     * @param exportDto 导出参数
     */
    @ApiOperation(value="导出报表", notes="导出报表")
    @SneakyThrows
    @PostMapping("/{version}/pt/export")
    public void export(@PathVariable String version, @RequestBody @Valid ExportDto exportDto){

        String fileType = exportDto.getFileType();
        String fileName = exportDto.getFileName();
        Integer dispRatio = exportDto.getDispRatio();
        Boolean pageBroken = exportDto.getPageBroken();
        Boolean graphicOut = exportDto.getGraphicOut();
        Boolean exportFormula = exportDto.getExportFormula();
        QueryDto queryDto = exportDto.getQueryDto();

        if(fileType == null || fileType.trim() == ""){
            throw new MedicalBusinessException("文件格式不允许空!");
        }
        if (fileName == null || fileName.trim() == ""){
            throw new MedicalBusinessException("文件名不允许为空!");
        }
        if(queryDto == null){
            throw new MedicalBusinessException("查询参数不允许为空!");
        }

        if(pageBroken == null){
            pageBroken = false;
        }
        if(graphicOut == null){
            graphicOut = false;
        }
        if(exportFormula == null){
            exportFormula = false;
        }
        if(dispRatio == null || dispRatio <= 1){
            dispRatio = 100;
        }

        IReport pageReport = getPage(queryDto);
        httpServletResponse.setContentType("application/x-download");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

        switch (fileType.toLowerCase()){
            case "xlsx":
            case "xls":
                ReportUtils.exportToExcel(servletOutputStream,pageReport, pageBroken, dispRatio,exportFormula);
                break;
            case "pdf":
                ReportUtils.exportToPDF(servletOutputStream,pageReport,pageBroken,graphicOut);
                break;
            case "doc":
                ReportUtils.exportToDOCX(servletOutputStream,pageReport);
                break;
            case "txt":
                ReportUtils.exportToText(servletOutputStream,pageReport);
                break;
            default:
                throw new MedicalBusinessException("不支持的文件格式:[{"+fileType+"}]");
        }
    }

    /**
     * 获取模板
     * @param id 模板ID
     * @return 报表模板定义
     */
    @SneakyThrows
    private ReportDefineProxy getTemplate(Long id){
        TemplateDto template = templateService.getOne(id);
        byte[] syntax = template.getSyntax();
        byte[] args = template.getArgs();
        @Cleanup InputStream reportStream = new ByteArrayInputStream(syntax);
        ReportDefine reportDefine = (ReportDefine) ReportUtils.read(reportStream);

        @Cleanup InputStream argsStream = new ByteArrayInputStream(args);
        ReportDefine argsDefine = (ReportDefine) ReportUtils.read(argsStream);

        ReportDefineProxy reportDefineProxy = new ReportDefineProxy();
        reportDefineProxy.setReportDefine(reportDefine);
        reportDefineProxy.setParametersDefine(argsDefine);

        return reportDefineProxy;
    }

    /**
     * 获取报表
     * @param id 模板ID
     * @return 报表
     */
    @SneakyThrows
    private IReport getReport(Long id, Map<?,?> parameters) {
        TemplateDto template = templateService.getOne(id);
        byte[] syntax = template.getSyntax();
        @Cleanup InputStream temp = new ByteArrayInputStream(syntax);
        ReportDefine reportDefine = (ReportDefine) ReportUtils.read(temp);

        Long datasourceId = template.getDatasourceId();
        DatasourceDto datasourceDto = dataSourceService.getOne(datasourceId);
        String datasourceName = datasourceDto.getName();

        //设置上下文
        //Context context = Context.getInitCtx();
        Context context = new Context();
        //设置参数
        if(parameters != null && !parameters.isEmpty()) {
            context.setParamMap(parameters);
        }

        //设置数据源
        IConnectionFactory factory = context.getConnectionFactory(datasourceName);

        if(factory == null){
            DataSource dataSource = objectMapper.readValue(datasourceDto.getConfig(), DataSource.class);
            dataSource.setFromType(DataSource.FROM_REMOTE);
            context.setConnectionFactory(datasourceName, dataSource);
        }

        context.setShowSQL(true);
        Engine engine = new Engine(reportDefine, context);
        return engine.calc();
    }

    /**
     * 获取页面
     * @param queryDto
     * @return
     */
    @SneakyThrows
    private IReport getPage(QueryDto queryDto){
        long templateId = queryDto.getId();
        PageSet pageSet = queryDto.getPageSet();
        Map<?,?> parameters = queryDto.getParameters();

        IReport iReport = getReport(templateId, parameters);
        IReport page = iReport;
        iReport.getRowCount();
        if(pageSet != null ){
            int currentPage =  pageSet.getCurrentPage();
            if(currentPage <= 0) currentPage = 1;
            PagerInfo pagerInfo = new PagerInfo();
            pagerInfo.setPagerStyle(pageSet.getPageStyle());
            pagerInfo.setRowNumPerPage(pageSet.getRowNumPerPage());
            pagerInfo.setPaperSize(pageSet.getPaperWidth(),pageSet.getPaperHeight());
            PageBuilder pageBuilder = new PageBuilder(iReport,pagerInfo);
            page = pageBuilder.getPage(currentPage);
        }
        return page;
    }

    /**
     * 获取html报表
     * @param queryDto
     * @return
     */
    @SneakyThrows
    private HtmlReport getHtmlReport(QueryDto queryDto) {
        IReport pageReport = getPage(queryDto);
        HtmlReport htmlReport = new HtmlReport(pageReport, "reportDS.htm");

        String appRoot = httpServletRequest.getScheme() + "://" +
                httpServletRequest.getServerName() + ":" +
                httpServletRequest.getServerPort() +
                httpServletRequest.getContextPath();

        htmlReport.setAppRoot(appRoot);

        return htmlReport;
    }

    /**
     * 分页参数
     */
    @ApiModel("分页参数")
    @Data
    public static class PageSet {

        public static final byte PAGE_STYLE_NONE  = PrintSetup.PAGER_NONE;
        public static final byte PAGE_STYLE_ROW  = PrintSetup.PAGER_ROW;
        public static final byte PAGE_STYLE_SIZE  = PrintSetup.PAGER_SIZE;

        @ApiModelProperty(name = "pageStyle", value = "分页方式", dataType = "Byte", allowableValues = "0,1,2")
        private Byte pageStyle ;

        @ApiModelProperty(name = "paperWidth", value = "页面宽度", dataType = "Float")
        private Float paperWidth;

        @ApiModelProperty(name = "paperHeight", value = "页面高度", dataType = "Float")
        private Float paperHeight;

        @ApiModelProperty(name = "rowNumPerPage",  value = "每页行数", dataType = "Integer")
        private Integer rowNumPerPage;

        @ApiModelProperty(name = "currentPage", value = "翻页页码", dataType = "Integer")
        private Integer currentPage = 1;

    }

    /**
     * 查询参数
     */
    @Data
    @ApiModel("查询参数")
    public static class QueryDto{
        @NotNull(message = "模板标识不允许为空")
        @ApiModelProperty(name = "id", value = "模板标识", dataType = "Long")
        private Long id;

        @ApiModelProperty(name = "parameters", value = "查询参数", dataType = "Map")
        private Map parameters;

        @ApiModelProperty(name = "pageSet", value = "分页设置", dataType = "com.iflytek.zhyl.valhalla.service.PageSet")
        private PageSet pageSet;
    }

    /**
     * 导出参数
     */
    @Data
    @ApiModel("导出参数")
    public static class  ExportDto {

        @NotNull(message = "导出文件类型不允许为空")
        @ApiModelProperty(name = "type", value = "导出文件类型", dataType = "String", allowableValues = "pdf,xls,xlsx,doc,txt")
        String fileType;

        @NotNull
        @ApiModelProperty(name = "fileName", value = "导出文件名称", dataType = "String")
        String fileName;

        @ApiModelProperty(name = "dispRatio", value = "导出文件缩放比例(默认100)",dataType = "Integer")
        Integer dispRatio;

        @ApiModelProperty(name = "pageBroken", value = "是否分页", dataType = "Boolean")
        Boolean pageBroken;

        @ApiModelProperty(name = "exportFormula", value = "是否输出公式", dataType = "Boolean")
        Boolean exportFormula;

        @ApiModelProperty(name = "graphicOut", value = "是否输出图片", dataType = "Boolean")
        Boolean graphicOut;

        @ApiModelProperty(name = "queryDto", value = "查询参数", dataType = "com.iflytek.zhyl.valhalla.service.QueryDto")
        QueryDto queryDto;
    }

    /**
     * 报表定义代理
     */
    @Data
    @ApiModel("报表定义代理")
    public static class ReportDefineProxy{

        private transient ReportDefine reportDefine;
        private transient ReportDefine parametersDefine;
        private transient ParamMetaData paramMetaData;
        private List<Param> paramList;

        public  ReportDefineProxy(){

        }
        public void ReportDefineProxy(ReportDefine reportDefine, ReportDefine argumentDefine){
            this.setReportDefine(reportDefine);
            this.setParametersDefine(argumentDefine);
        }

        public void setReportDefine(ReportDefine reportDefine){
            this.reportDefine = reportDefine;
            this.paramMetaData = reportDefine.getParamMetaData();
            if(paramMetaData != null) {
                int paramCount = this.paramMetaData.getParamCount();
                if (paramCount > 0) {
                    paramList = new ArrayList<>();
                    for (int i = 0; i < paramCount; i++) {
                        paramList.add(paramMetaData.getParam(i));
                    }
                }
            }
        }
        public void setParametersDefine(ReportDefine parametersDefine){
            this.parametersDefine = parametersDefine;
        }

    }
}
