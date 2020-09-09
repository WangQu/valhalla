package com.iflytek.zhyl.valhalla.service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.medicalboot.core.dto.PageData;
import com.iflytek.medicalboot.core.dto.PageRequest;
import com.iflytek.medicalboot.core.exception.MedicalBusinessException;
import com.iflytek.medicalboot.core.id.BatchUidService;
import com.iflytek.zhyl.valhalla.entity.TbDatasource;
import com.iflytek.zhyl.valhalla.pojo.DatasourceDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQueryFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.iflytek.zhyl.valhalla.entity.QTbDatasource.qTbDatasource;
import static com.iflytek.zhyl.valhalla.entity.QTbTemplate.qTbTemplate;

@RestController
@Slf4j
@Api(tags = "数据源管理")
@Service
public class DataSourceService {

    @Autowired
    private SQLQueryFactory queryFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BatchUidService batchUidService;

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "新增数据源信息")
    @PostMapping("/{version}/pt/TbDatasource")
    public Long postOne(@RequestBody TbDatasource datasource) throws Exception {
        log.info("开始新增数据源信息，信息:[{}]",datasource);

        this.checkRequiredFields(datasource);
        this.checkDuplicate(datasource,null);
        String config = datasource.getConfig();
        datasource.setConfig(config);

        Long id = batchUidService.getUid("TB_DATASOURCE");
        datasource.setId(id);
        datasource.setEnabled(1);
        datasource.setCreateTime(new Date());

        Long result = queryFactory.insert(qTbDatasource).
                populate(datasource).executeWithKey(qTbDatasource.id);
        log.info("结束新增数据源信息[{}]，结果 [{}]", datasource, result);
        return result;
    }

    @ApiOperation(value = "查询数据源列表")
    @GetMapping("/{version}/pt/TbDatasource")
    public PageData<DatasourceDto, DatasourceFilter> getList(DatasourcePageRequest pageRequest){
        log.info("开始查询数据源信息列表，查询条件：[{}]",pageRequest);
        if(pageRequest.getPageNumber() == null && pageRequest.getPageSize() == null){
            pageRequest.setPageNumber(1);
            pageRequest.setPageSize(Integer.MAX_VALUE);
        }

        List<Predicate> expressions = new ArrayList<>();
        DatasourceFilter filter = pageRequest.getFilter();

        if (filter != null) {
            if (StringUtils.hasText(filter.getKeyword())) {
                String keyword = "%" + filter.getKeyword() + "%";
                expressions.add(qTbDatasource.config.likeIgnoreCase(keyword));
            }
        }

        QueryResults<TbDatasource> queryResult = queryFactory.select(qTbDatasource)
                .from(qTbDatasource)
                .where(expressions.toArray(new Predicate[0]))
                .limit(pageRequest.getPageSize())
                .offset((pageRequest.getPageIndex() - 1) * pageRequest.getPageSize())
                .fetchResults();
        if (pageRequest.needCount()) {
            pageRequest.setTotal(queryResult.getTotal());
        }
        log.info("结束查询数据源信息分页列表，结果条数：[{}]", queryResult.isEmpty() ? 0 : queryResult.getResults().size());
        return new PageData<>(queryResult.getResults(), pageRequest.buildNextPage());
    }

    @ApiOperation(value = "查询数据源列表")
    @PostMapping("/{version}/pt/TbDatasource/search")
    public PageData<DatasourceDto, DatasourceFilter> searchList(@RequestBody DatasourcePageRequest pageRequest) {
        return this.getList(pageRequest);
    }

    @ApiOperation(value = "查询数据源详细信息")
    @GetMapping("/{version}/pt/TbDatasource/{id}")
    @Cached(name = "datasourceCache_",expire = 3600, cacheType = CacheType.LOCAL)
    public DatasourceDto getOne(@PathVariable Long id) {
        HttpServletRequest httpServletRequest =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        log.info(httpServletRequest.getParameterMap().toString());
        log.info(httpServletRequest.getRemoteAddr());
        log.info(httpServletRequest.getRemoteHost());

        log.info("开始查询数据源详细信息，主键：[{}]", id);
        DatasourceDto resource = queryFactory.select(DatasourceDto.Q_BEAN)
                .from(qTbDatasource)
                .where(qTbDatasource.id.eq(id))
                .fetchOne();
        log.info("结束查询数据源详细信息，主键：[{}]，信息：[{}]", id, resource);
        return resource;
    }

    @ApiOperation(value = "查询数据源详细信息")
    @GetMapping("/{version}/pt/TbDatasource/getOneByName/{name}")
    public DatasourceDto getOneByName(@PathVariable String name){
        log.info("开始查询数据源详细信息，名称：[{}]", name);
        DatasourceDto resource = queryFactory.select(DatasourceDto.Q_BEAN)
                .from(qTbDatasource)
                .where(qTbDatasource.name.eq(name))
                .fetchOne();
        log.info("结束查询数据源详细信息，名称：[{}]，信息：[{}]", name, resource);
        return resource;
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改数据源")
    @PutMapping("/{version}/pt/TbDatasource/{id}")
    @CacheUpdate(name = "datasourceCache_", key = "#id", value = "datasource")
    public long putOne(@PathVariable Long id, @RequestBody TbDatasource datasource) throws Exception {
        log.info("开始修改数据源，信息：[{}]", datasource);
        this.checkRequiredFields(datasource);
        this.checkDuplicate(datasource, id);
        datasource.setId(id);

        DatasourceDto old = this.getOne(id);
        if (old == null) {
            log.info("待修改的数据源 [{}] 不存在", id);
            return 0L;
        }
        datasource.setModifyTime(new Date());
        long result = queryFactory.update(qTbDatasource)
                .populate(datasource)
                .where(qTbDatasource.id.eq(id)).execute();
        log.info("结束修改报表，信息：[{}]，结果：[{}]", datasource, result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "删除数据源")
    @DeleteMapping("/{version}/pt/TbDatasource/{id}")
    @CacheInvalidate(name = "datasourceCache_",key = "#id")
    public long deleteOne(@PathVariable Long id) {
        log.info("开始删除报表 [{}]", id);

        this.checkDelete(id);

        long result = queryFactory.update(qTbDatasource)
                .set(qTbDatasource.modifyTime, new Date())
                .set(qTbDatasource.enabled, 1)
                .where(qTbDatasource.id.eq(id))
                .execute();
        log.info("结束删除报表 [{}]，结果 [{}]", id, result);
        return result;
    }
    
    /**
     * 获取所有数据源
     * @return 数据源集合
     */
    public List<DatasourceDto> getAll() {
        return queryFactory.select(DatasourceDto.Q_BEAN)
                .from(qTbDatasource)
                .fetch();
    }
    
    /**
     * 检查必填字段
     * @param datasource 实体数据
     */
    private void checkRequiredFields(TbDatasource datasource) throws IOException {
        if (StringUtils.isEmpty(datasource.getName()) ) {
            throw new MedicalBusinessException("数据源名称和编码不能为空");
        }
        Map config = objectMapper.readValue(datasource.getConfig(),Map.class);


        int dbType = (int) config.get("dbType");
        if (StringUtils.isEmpty(dbType)) {
            throw new MedicalBusinessException("数据源数据源类型不能为空");
        }

        if(dbType != 0){
            if (StringUtils.isEmpty(config.get("driver")) ) {
                throw new MedicalBusinessException("数据源驱动不能为空");
            }
            if (StringUtils.isEmpty(config.get("url")) ) {
                throw new MedicalBusinessException("数据源链接不能为空");
            }
            if (StringUtils.isEmpty(config.get("user")) ) {
                throw new MedicalBusinessException("数据源用户名不能为空");
            }
            if (StringUtils.isEmpty(config.get("password")) ) {
                throw new MedicalBusinessException("数据源密码不能为空");
            }
        }
    }

    /**
     * 检查编码重复
     * @param datasource 实体数据
     * @param id           主键，可选。更新操作时，用于排除要更新的数据本身。
     */
    private void checkDuplicate(TbDatasource datasource, Long id) {
        List<Predicate> expressions = new ArrayList<>();
        expressions.add(qTbDatasource.name.equalsIgnoreCase(datasource.getName()));
        if (id != null && id > 0) {
            expressions.add(qTbDatasource.id.ne(id));
        }
        long count = queryFactory.selectFrom(qTbDatasource)
                .where(expressions.toArray(new Predicate[0]))
                .fetchCount();
        if (count > 0) {
            throw new MedicalBusinessException("数据源名称或id已存在");
        }
    }

    private void checkDelete(Long id){
        List<Predicate> expressions = new ArrayList<>();
        expressions.add(qTbTemplate.datasourceId.eq(id));
        expressions.add(qTbTemplate.enabled.eq(1));
        long count = queryFactory.selectFrom(qTbTemplate)
                .where(expressions.toArray(new Predicate[0]))
                .fetchCount();
        if(count > 0){
            throw new MedicalBusinessException("数据源被[" + count+ "]个模板引用，请检查！");
        }
    }

    @Data
    public static class DatasourceFilter {

        @ApiModelProperty("模糊匹配关键字")
        private String keyword;

        @ApiModelProperty("是否可用")
        private String enabled;
    }

    public static class DatasourcePageRequest extends PageRequest<DatasourceFilter> {
    }
}
