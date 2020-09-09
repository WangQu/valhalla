package com.iflytek.zhyl.valhalla.pojo;

import com.iflytek.zhyl.valhalla.entity.TbTemplate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.iflytek.zhyl.valhalla.entity.QTbTemplate.qTbTemplate;

@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateDto extends TbTemplate {
    public static final QBean<TemplateDto> Q_BEAN = Projections.bean(TemplateDto.class, qTbTemplate.all());

}
