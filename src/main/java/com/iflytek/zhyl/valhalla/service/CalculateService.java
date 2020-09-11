package com.iflytek.zhyl.valhalla.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.medicalboot.core.id.BatchUidService;
import com.iflytek.zhyl.valhalla.entity.TbTemporary;
import com.querydsl.sql.SQLQueryFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.Map;

import static com.iflytek.zhyl.valhalla.entity.QTbTemporary.qTbTemporary;


@Slf4j
@RestController
@Service
@Api(tags = "计算服务")
public class CalculateService {
    @Autowired
    private SQLQueryFactory queryFactory;

    @Autowired
    private BatchUidService batchUidService;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/{version}/pt/data")
    @ApiOperation(value="输入数据", notes="输入数据")
    public Long postData(@PathVariable String version, @RequestBody Map<String,Object> map){
        Long id = batchUidService.getUid("TB_TEMPORARY");
        TbTemporary tbTemporary = new TbTemporary();

        tbTemporary.setId(id);
        tbTemporary.setData(JSONObject.toJSONString(map));
        tbTemporary.setCreateTime(new Date());
        Long result = queryFactory.insert(qTbTemporary).
                populate(tbTemporary).executeWithKey(qTbTemporary.id);
        log.info("结束新增临时数据[{}]，结果 [{}]", tbTemporary, result);
        return result;
    }



}