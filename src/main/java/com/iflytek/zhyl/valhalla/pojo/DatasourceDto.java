package com.iflytek.zhyl.valhalla.pojo;

import com.iflytek.zhyl.valhalla.entity.TbDatasource;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.iflytek.zhyl.valhalla.entity.QTbDatasource.qTbDatasource;

@EqualsAndHashCode(callSuper = true)
@Data
public class DatasourceDto extends TbDatasource {

    public static final QBean<DatasourceDto> Q_BEAN = Projections.bean(DatasourceDto.class, qTbDatasource.all());

}
