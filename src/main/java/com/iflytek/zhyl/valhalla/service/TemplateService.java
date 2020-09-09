package com.iflytek.zhyl.valhalla.service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.iflytek.medicalboot.core.dto.PageData;
import com.iflytek.medicalboot.core.dto.PageRequest;
import com.iflytek.medicalboot.core.exception.MedicalBusinessException;
import com.iflytek.medicalboot.core.id.BatchUidService;
import com.iflytek.zhyl.valhalla.entity.TbTemplate;
import com.iflytek.zhyl.valhalla.pojo.TemplateDto;
import com.iflytek.zhyl.valhalla.utils.FileUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQueryFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.iflytek.zhyl.valhalla.entity.QTbTemplate.qTbTemplate;

@RestController
@Slf4j
@Api(tags = "模板管理")
@Service
public class TemplateService {
    @Autowired
    private SQLQueryFactory queryFactory;

    @Autowired
    private BatchUidService batchUidService;

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "新增报表信息")
    @PostMapping("/{version}/pt/TbTemplate")
    public Long postOne(@RequestBody TbTemplate tbTemplate)  {
        log.info("开始新增报表信息，信息:[{}]",tbTemplate);

        this.checkRequiredFields(tbTemplate);
        this.checkDuplicate(tbTemplate,null);

        Long id = batchUidService.getUid("TB_TEMPLATE");
        tbTemplate.setId(id);
        tbTemplate.setEnabled(1);
        tbTemplate.setCreateTime(new Date());

        Long result = queryFactory.insert(qTbTemplate).
                populate(tbTemplate).executeWithKey(qTbTemplate.id);

        log.info("结束新增报表信息[{}]，结果 [{}]", tbTemplate, result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "上传报表模板")
    @PostMapping("/{version}/pt/TbTemplate/upload/{id}")
    @CacheInvalidate(name = "templateCache_",key = "#id")
    public Long upload(@PathVariable Long id,
                       @RequestPart(value = "syntax", required = false) @ApiParam(value = "报表文件(.rpx)") MultipartFile templateFile,
                       @RequestPart(value = "args", required = false) @ApiParam(value = "参数文件(.rpx)") MultipartFile argsFile) throws Exception {

        boolean beModified = false;
        TbTemplate tbTemplate = getOne(id);

        InputStream inputStream;
        if(templateFile != null && !templateFile.isEmpty()) {
            inputStream = templateFile.getInputStream();
            byte[] syntax = FileUtils.getFile(inputStream);
            if(syntax.length >0 ) {
                tbTemplate.setSyntax(syntax);
                beModified = true;
            }
            inputStream.close();
        }

        if(argsFile != null && !argsFile.isEmpty()) {
            inputStream = argsFile.getInputStream();
            byte[] args = FileUtils.getFile(inputStream);
            if(args.length >0 ) {
                tbTemplate.setArgs(args);
                beModified = true;
            }
            inputStream.close();
        }

        if(beModified) {
            putOne(id, tbTemplate);
        }

        return 0L;
    }

    @ApiOperation(value = "查询报表列表")
    @GetMapping("/{version}/pt/TbTemplate")
    public PageData<TemplateDto, TemplateFilter> getList(TemplatePageRequest pageRequest){
        log.info("开始查询报表信息列表，查询条件：[{}]",pageRequest);
        if(pageRequest.getPageNumber() == null && pageRequest.getPageSize() == null){
            pageRequest.setPageNumber(1);
            pageRequest.setPageSize(Integer.MAX_VALUE);
        }

        List<Predicate> expressions = new ArrayList<>();
        TemplateFilter filter = pageRequest.getFilter();

        if (filter != null) {
            if (StringUtils.hasText(filter.getKeyword())) {
                String keyword = "%" + filter.getKeyword() + "%";
                expressions.add(qTbTemplate.name.likeIgnoreCase(keyword));
            }
        }

        QueryResults<TbTemplate> queryResult = queryFactory.select(qTbTemplate)
                .from(qTbTemplate)
                .where(expressions.toArray(new Predicate[0]))
                .limit(pageRequest.getPageSize())
                .offset((pageRequest.getPageIndex() - 1) * pageRequest.getPageSize())
                .fetchResults();
        if (pageRequest.needCount()) {
            pageRequest.setTotal(queryResult.getTotal());
        }
        log.info("结束查询报表信息分页列表，结果条数：[{}]", queryResult.isEmpty() ? 0 : queryResult.getResults().size());
        return new PageData<>(queryResult.getResults(), pageRequest.buildNextPage());
    }

    @ApiOperation(value = "查询报表列表")
    @PostMapping("/{version}/pt/TbTemplate/search")
    public PageData<TemplateDto, TemplateFilter> searchList(@RequestBody TemplatePageRequest pageRequest) {
        return this.getList(pageRequest);
    }

    @ApiOperation(value = "查询报表详细信息")
    @GetMapping("/{version}/pt/TbTemplate/{id}")
    @Cached(name = "templateCache_",expire = 60, cacheType = CacheType.LOCAL)
    public TemplateDto getOne(@PathVariable Long id) {
        log.info("开始查询报表详细信息，主键：[{}]", id);
        TemplateDto resource = queryFactory.select(TemplateDto.Q_BEAN)
                .from(qTbTemplate)
                .where(qTbTemplate.id.eq(id))
                .fetchOne();
        log.info("结束查询报表详细信息，主键：[{}]，信息：[{}]", id, resource);
        return resource;
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改报表")
    @PutMapping("/{version}/pt/TbTemplate/{id}")
    @CacheUpdate(name = "templateCache_", key = "#id", value = "tbTemplate")
    public long putOne(@PathVariable Long id,
                       TbTemplate tbTemplate) throws IOException {
        log.info("开始修改报表，信息：[{}]", tbTemplate);
        this.checkRequiredFields(tbTemplate);
        this.checkDuplicate(tbTemplate, id);

        tbTemplate.setId(id);
        tbTemplate.setModifyTime(new Date());
        long result = queryFactory.update(qTbTemplate)
                .populate(tbTemplate)
                .where(qTbTemplate.id.eq(id)).execute();

        log.info("结束修改报表，信息：[{}]，结果：[{}]", tbTemplate, result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "删除报表")
    @DeleteMapping("/{version}/pt/TbTemplate/{id}")
    @CacheInvalidate(name = "templateCache_",key = "#id")
    public long deleteOne(@PathVariable Long id) {
        log.info("开始删除报表 [{}]", id);
        long result = queryFactory.update(qTbTemplate)
                .set(qTbTemplate.modifyTime, new Date())
                .set(qTbTemplate.enabled, 0)
                .where(qTbTemplate.id.eq(id))
                .execute();
        log.info("结束删除报表 [{}]，结果 [{}]", id, result);
        return result;
    }

    /**
     * 获取所有报表
     * @return 报表集合
     */
    public List<TemplateDto> getAll() {
        return queryFactory.select(TemplateDto.Q_BEAN)
                .from(qTbTemplate)
                .fetch();
    }

    /**
     * 检查必填字段
     * @param tbTemplate 实体数据
     */
    private void checkRequiredFields(TbTemplate tbTemplate) {
        if (StringUtils.isEmpty(tbTemplate.getName()) ) {
            throw new MedicalBusinessException("报表名称不能为空");
        }
    }

    /**
     * 检查编码重复
     * @param tbTemplate 实体数据
     * @param id           主键，可选。更新操作时，用于排除要更新的数据本身。
     */
    private void checkDuplicate(TbTemplate tbTemplate, Long id) {
        List<Predicate> expressions = new ArrayList<>();
        expressions.add(qTbTemplate.name.equalsIgnoreCase(tbTemplate.getName()));
        if (id != null && id > 0) {
            expressions.add(qTbTemplate.id.ne(id));
        }
        long count = queryFactory.selectFrom(qTbTemplate)
                .where(expressions.toArray(new Predicate[0]))
                .fetchCount();
        if (count > 0) {
            throw new MedicalBusinessException("报表名称或id已存在");
        }
    }

    @Data
    public static class TemplateFilter {

        @ApiModelProperty("模糊匹配关键字")
        private String keyword;

        @ApiModelProperty("是否可用")
        private String enabled;
    }

    public static class TemplatePageRequest extends PageRequest<TemplateFilter> {
    }
}
